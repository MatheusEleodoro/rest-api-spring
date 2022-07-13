package com.eleodoro.dev.service;

import com.eleodoro.dev.form.RequestForm;
import com.eleodoro.dev.form.ResultStatus;
import com.eleodoro.dev.model.AreaUsuario;
import com.eleodoro.dev.model.GitRepositorio;
import com.eleodoro.dev.model.Usuario;
import com.eleodoro.dev.repository.UsuarioRepository;
import com.eleodoro.dev.utils.FilterCriteria;
import com.eleodoro.dev.utils.Servant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements Servant {
    @Autowired
    UsuarioRepository repository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    Environment env;
    Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    public static List<GitRepositorio> gitRepositorios = new ArrayList<>();

    public ResponseEntity<Page<Usuario>> findUsuario(Pageable pageable, FilterCriteria filter, RequestForm form)
    {
        return ResponseEntity.ok(repository.findAll(
                Specification
                        .where(filter.toEquals(form.id,"id"))
                        .and(filter.toLike(form.nome,"nome"))
                        .and(filter.toLike(form.email,"email")
                        .and(filter.toEquals(form.idAreaUsuario,"areaDeUsuario")))
                        .and(filter.toEqualsInParent(form.responsavel,"areaDeUsuario","responsavel")),
                pageable
        ));
    }

    @Transactional
    public ResultStatus saveUsuario(@NotNull RequestForm form)
    {
        try
        {
            repository.save(new Usuario(form.nome,form.email, encoder.encode(form.senha), new AreaUsuario(form.idAreaUsuario)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ResultStatus(true,e.getMessage(),304);
        }

        return new ResultStatus(false,"Usuario cadastrado com sucesso",200);
    }

    public Optional<Usuario> findByEmail(String email)
    {
        var res = repository.findByEmail(email);
        return  res;
    }

    public  ResponseEntity<List<GitRepositorio>> getRepositorios(String usuario)
    {
        ObjectMapper oMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        logger.info(env.getProperty("system.app.teste"));
        if(gitRepositorios.isEmpty())
        {
            var res = restTemplate.getForObject(String.format("https://api.github.com/users/%s/repos",usuario),Object.class);
            List<LinkedHashMap> infos = (List<LinkedHashMap>)res;
            var names = infos.stream().map(it->it.get("name")).collect(Collectors.toList());

            logger.info("buscando novos dados no repositorio do usuario: "+usuario);
            names.stream().forEach(name->{
                try
                {
                    var readme =
                            restTemplate.getForObject(String.format("https://raw.githubusercontent.com/%s/%s/main/README.md",usuario,name.toString()),String.class);
                    if(readme!=null)
                        gitRepositorios.add(new GitRepositorio(name.toString(),readme));

                }catch (Exception e){}

            });
        }
        return ResponseEntity.ok(gitRepositorios.stream().filter(it->!it.getTitle().equals("MatheusEleodoro")).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Page<?>> findAll(Pageable pageable, FilterCriteria filter, RequestForm form) throws ParseException {
        return null;
    }

    @Override
    public ResultStatus save(RequestForm form, Authentication auth) {
        return null;
    }

    @Override
    public ResultStatus alterar(RequestForm form, Authentication auth) {
        return null;
    }

    @Override
    public ResultStatus deletar(RequestForm form, Authentication auth) {
        return null;
    }
}
