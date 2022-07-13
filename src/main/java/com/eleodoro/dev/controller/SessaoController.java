package com.eleodoro.dev.controller;
import com.eleodoro.dev.form.RequestForm;
import com.eleodoro.dev.service.SessaoService;
import com.eleodoro.dev.utils.FilterCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/*")
public class SessaoController {
    @Autowired
    SessaoService service;

    @GetMapping("pesquisarSessao")
    public ResponseEntity<Page<?>> findAll(Pageable pageable, FilterCriteria filter, @RequestBody RequestForm form)
    {
        try{
            return service.findAll(pageable,filter,form);
        }catch (Exception e)
        {
            return null;
        }

    }


}
