package com.eleodoro.dev.model;

import com.eleodoro.dev.form.ResultStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Logout
 * @author Matheus Eleodoro
 */
public class Logout implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(writer.writeValueAsString(new ResultStatus(false,"Logout Efetuado com sucesso",200)));
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();;
    }
}
