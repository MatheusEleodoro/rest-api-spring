package com.eleodoro.dev.security;

import com.eleodoro.dev.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Provedor de Autenticação
 * @author Matheus Eleodoro
 *
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	UsuarioService service;
	@Autowired private PasswordEncoder encoder;
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String email = authentication.getPrincipal().toString();
		String password = authentication.getCredentials()==null?"":authentication.getCredentials().toString();
		
		var user = service.findByEmail(email);
		
		if(user == null || user.get().getPassword()==null )
			throw new BadCredentialsException("404:"+email);
		if(!encoder.matches(password, user.get().getPassword()))
			throw new BadCredentialsException("401");
		
		 return new UsernamePasswordAuthenticationToken(user.get().getEmail(),user.get().getPassword(),
				user.get().getPermissoes());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
