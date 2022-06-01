package com.eleodoro.dev.service;

import com.eleodoro.dev.form.ResultStatus;
import com.eleodoro.dev.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class LoginService {
    @Autowired
    UsuarioRepository repository;
    @Autowired
    SessaoService sessaoService;

    public ResponseEntity<?> efetuarLogin(String email, String senha,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        if(validaAcesso(email, senha,response))
            return ResponseEntity.status(HttpStatus.OK).body(new ResultStatus(false,"login efetuado",200));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResultStatus(true,"login não autorizado",200));
    }

    private boolean validaAcesso(String email,String senha,HttpServletResponse response)
    {
        var token = response.getHeader("Authorization");
        if(token==null)
            return false;

        AtomicBoolean res = new AtomicBoolean(false);
        repository.findByEmail(email).ifPresent(user->{
            res.set(user.getPassword().equals(senha));

            if(!sessaoService.save(user,token))
                res.set(false);

        });


        return res.get();
    }
}
