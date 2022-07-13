package com.eleodoro.dev.controller;

import com.eleodoro.dev.form.RequestForm;
import com.eleodoro.dev.form.ResultStatus;
import com.eleodoro.dev.model.GitRepositorio;
import com.eleodoro.dev.model.Usuario;
import com.eleodoro.dev.service.UsuarioService;
import com.eleodoro.dev.utils.FilterCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/*")
public class UsuarioController {
    @Autowired
    UsuarioService service;

    @GetMapping("pesquisarUsuario")
    public ResponseEntity<Page<Usuario>> findAll(Pageable pageable, FilterCriteria filter, @RequestBody RequestForm form)
    {
        return service.findUsuario(pageable,filter,form);
    }

    @PostMapping("incluirUsuario")
    public ResultStatus incluir(@RequestBody RequestForm form)
    {
        return service.saveUsuario(form);
    }

    @GetMapping("pesquisarUsuarioRepositorio")
    public ResponseEntity<List<GitRepositorio>> gitRepository(@RequestParam String usuario)
    {
        return service.getRepositorios(usuario);
    }
}
