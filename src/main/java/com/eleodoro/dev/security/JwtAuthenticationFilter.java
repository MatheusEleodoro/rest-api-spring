package com.eleodoro.dev.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eleodoro.dev.config.WebConfig;
import com.eleodoro.dev.form.RequestForm;
import com.eleodoro.dev.form.ResultStatus;
import com.eleodoro.dev.service.LoginService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


/**
 * Filtros das requisições
 * @author Matheus Eleodoro
 *
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final JwtAuthenticationProvider authenticationManager;
	private final LoginService loginService;

	public JwtAuthenticationFilter(JwtAuthenticationProvider authenticationManager, LoginService loginService) {
		this.authenticationManager = authenticationManager;
		this.loginService = loginService;
	}

	// Trigger e disparado quando recebe o chamada POST do /login
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		RequestForm form = null;
		try {
			form = new ObjectMapper().readValue(request.getInputStream(), RequestForm.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create login token
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				form.email,
				form.senha,
				new ArrayList<>());


		// Authenticate user
		Authentication auth = authenticationManager.authenticate(authenticationToken);

		return auth;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

		// Cria token jwt
		String token = JWT.create()
				.withSubject(authResult.getPrincipal().toString())
				.withExpiresAt(new Date(System.currentTimeMillis() + WebConfig.EXPIRATION))
				.sign(HMAC512(WebConfig.SECRET.getBytes()));

		response.addHeader("Authorization", WebConfig.TOKEN_PREFIX +  token);
		ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
		var res = loginService.efetuarLogin(
				authResult.getPrincipal().toString(),
				authResult.getCredentials().toString(),
				request,
				response);

		// Adiciona token no header da requisição de login
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(objectWriter.writeValueAsString(res.getBody()));
		response.setStatus(res.getStatusCodeValue());
		response.getWriter().flush();
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
		
		if(failed.getMessage().contains("401"))
		{
			response.getWriter()
			.write(objectWriter.writeValueAsString(new ResultStatus(true,"Informações de login invalidas",401)));
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().flush();
			return;
		}
		if(failed.getMessage().contains("404")||failed.getMessage().contains("Usuario não encontrado"))
		{
			var email = failed.getMessage().split(":")[1];
			var res = loginService.efetuarLogin(
					email,
					null,
					request,
					response
					);
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(objectWriter.writeValueAsString(res.getBody()));
			response.setStatus(res.getStatusCodeValue());
			response.getWriter().flush();
		}
	}

}