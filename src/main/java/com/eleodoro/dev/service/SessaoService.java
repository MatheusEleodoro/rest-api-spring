package com.eleodoro.dev.service;

import com.eleodoro.dev.form.RequestForm;
import com.eleodoro.dev.form.ResultStatus;
import com.eleodoro.dev.model.Sessao;
import com.eleodoro.dev.model.Usuario;
import com.eleodoro.dev.repository.SessaoRespository;
import com.eleodoro.dev.utils.FilterCriteria;
import com.eleodoro.dev.utils.Servant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SessaoService implements Servant {

    @Autowired
    private SessaoRespository repository;

    @Override
    public ResponseEntity<Page<?>> findAll(Pageable pageable, FilterCriteria filter, RequestForm form) throws ParseException {

        if(form.dataFim == null)
        {
            form.dataFim = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        return ResponseEntity.ok(repository.findAll(
                (Specification) Specification
                        .where(filter.toBetween(form.dataInicio,form.dataFim,"data", FilterCriteria.Type.DATE)),
                pageable));
    }

    @Override
    public ResultStatus save(RequestForm form, Authentication auth) {
        return null;
    }

    public boolean save(Usuario usuario,String token)
    {
        return repository.save(new Sessao(new Date(),usuario,token))!=null;
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
