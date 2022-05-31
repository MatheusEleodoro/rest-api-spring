package com.eleodoro.dev.service;

import com.eleodoro.dev.model.AreaUsuario;
import com.eleodoro.dev.model.Usuario;
import com.eleodoro.dev.repository.AreaUsuarioRepository;
import com.eleodoro.dev.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaUsuarioService {
    @Autowired
    private AreaUsuarioRepository repository;


    public ResponseEntity<List<AreaUsuario>> findAreaUsuario()
    {
        return ResponseEntity.ok(repository.findAll());
    }
}
