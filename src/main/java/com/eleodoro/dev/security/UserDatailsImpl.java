package com.eleodoro.dev.security;

import java.util.Collection;

import com.eleodoro.dev.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDatailsImpl implements UserDetails {

	private static final long serialVersionUID = 4934921851879971259L;

	private String username;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserDatailsImpl(Usuario user)
	{
		this.username = user.getEmail();
		this.password = user.getPassword();	
		this.authorities = user.getPermissoes();
	}
	
	public static UserDatailsImpl create(Usuario user)
	{
		return new UserDatailsImpl(user);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
