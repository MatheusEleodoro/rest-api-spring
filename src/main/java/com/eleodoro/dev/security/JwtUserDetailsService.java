package com.eleodoro.dev.security;

import com.eleodoro.dev.model.Usuario;
import com.eleodoro.dev.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * JwtUserDetails
 * @author Matheus Eleodoro
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	UsuarioService service;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario user = service.findByEmail(username).orElseThrow(()->
		new UsernameNotFoundException("Usuario ["+username+"] não encontrado"));

		return UserDatailsImpl.create(user);

	}
}
