package com.eleodoro.dev.utils;

import com.eleodoro.dev.form.RequestForm;
import com.eleodoro.dev.form.ResultStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;


public interface Servant {

    @Transactional
    public ResponseEntity<Page<?>> findAll(Pageable pageable,FilterCriteria filter, RequestForm form) throws ParseException;

    @Transactional
    public ResultStatus save(RequestForm form, Authentication auth);

    @Transactional
    public ResultStatus alterar(RequestForm form,Authentication auth);

    @Transactional
    public ResultStatus deletar(RequestForm form, Authentication auth);

    default void Teste(){
        System.out.println("TESTE");
    }
}
