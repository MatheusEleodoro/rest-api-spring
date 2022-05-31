package com.eleodoro.dev.service;

import com.eleodoro.dev.form.RequestForm;
import com.eleodoro.dev.form.ResultStatus;
import com.eleodoro.dev.model.AreaUsuario;
import com.eleodoro.dev.model.Usuario;
import com.eleodoro.dev.repository.UsuarioRepository;
import com.eleodoro.dev.utils.FilterCriteria;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository repository;

    @Autowired
    PasswordEncoder encoder;
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
}
