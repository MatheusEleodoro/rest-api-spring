package com.eleodoro.dev.repository;

import com.eleodoro.dev.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long>, JpaSpecificationExecutor {

    Optional<Usuario> findByEmail(String email);
}
