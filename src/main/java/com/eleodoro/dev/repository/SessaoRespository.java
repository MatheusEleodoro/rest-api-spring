package com.eleodoro.dev.repository;

import com.eleodoro.dev.model.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SessaoRespository extends JpaRepository<Sessao,Long>, JpaSpecificationExecutor<Long> {
}
