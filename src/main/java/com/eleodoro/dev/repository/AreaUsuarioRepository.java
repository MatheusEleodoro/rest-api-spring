package com.eleodoro.dev.repository;

import com.eleodoro.dev.model.AreaUsuario;
import com.eleodoro.dev.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaUsuarioRepository extends JpaRepository<AreaUsuario,Long> {
}
