package com.eleodoro.dev.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eleodoro.dev.config.WebConfig;
import com.eleodoro.dev.form.ResultStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


/**
 * Filtro de Autorizações do usuario
 * @author Matheus Eleodoro
 *
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private JwtUserDetailsService userDetailsService;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService) {
		super(authenticationManager);
		this.userDetailsService = userDetailsService;
	}

	// Trigger chamado quando recebe requisições de outros endpoints
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith(WebConfig.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}

		Authentication authentication = getUsernamePasswordAuthentication(request,response);
		SecurityContextHolder.getContext().setAuthentication(authentication);


		chain.doFilter(request, response);
	}

	private Authentication getUsernamePasswordAuthentication(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String token = request.getHeader("Authorization")
				.replace(WebConfig.TOKEN_PREFIX,"");

		if (token != null) {

			// valida se o token vindo da requisição é valido
			try 
			{
				String userName = JWT.require(Algorithm.HMAC512(WebConfig.SECRET.getBytes()))
						.build()
						.verify(token.replace(WebConfig.TOKEN_PREFIX, ""))
						.getSubject();

				// chama o service details p/ obter informações do usuario e autenticar junto com seus permissoes
				if (userName != null) {
					var principal = userDetailsService.loadUserByUsername(userName);
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userName, null, principal.getAuthorities());
					return auth;
				}
			}
			catch (Exception e) 
			{
				ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(objectWriter.writeValueAsString(new ResultStatus(true,"Sessão Expirada",401)));
				response.addHeader("Authorization", null);
				response.getWriter().flush();
			}

			return null;
		}
		return null;
	}
}