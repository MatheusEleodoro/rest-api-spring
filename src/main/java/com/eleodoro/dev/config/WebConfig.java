package com.eleodoro.dev.config;

import com.eleodoro.dev.model.Logout;
import com.eleodoro.dev.security.JwtAuthenticationFilter;
import com.eleodoro.dev.security.JwtAuthenticationProvider;
import com.eleodoro.dev.security.JwtAuthorizationFilter;
import com.eleodoro.dev.security.JwtUserDetailsService;
import com.eleodoro.dev.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableScheduling
@EnableGlobalMethodSecurity(
		prePostEnabled = true, 
		securedEnabled = true, 
		jsr250Enabled = true)
public class WebConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	public static final String SECRET = "710bdd68fbde92898a7ab33811feed157edd6505ca438e33de60b223c75ebb9e231f7c314e48b5a88ac8d61097992bb4e07c2550820aef0226de21e134cd0723";
	public static final Long EXPIRATION = 6000000L;
	public static final String TOKEN_PREFIX = "Bearer ";

	@Autowired private JwtUserDetailsService userPrincipalDetailsService;
	@Autowired private JwtAuthenticationProvider authenticationProvider;
	@Autowired private LoginService loginService;


	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider);
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		
		 // adicionando fitros nas requisiçoes 1º autenticacao, 2º permissoes
		.addFilter(new JwtAuthenticationFilter(authenticationProvider,loginService))
		.addFilter(new JwtAuthorizationFilter(authenticationManager(),  this.userPrincipalDetailsService))
		.authorizeRequests()
		
		 // configuração de acesso as rotas
		.antMatchers(HttpMethod.POST, "/main/api/grafico").permitAll()
				.antMatchers("/pesquisarUsuarioRepositorio").permitAll()
		.anyRequest().authenticated()
		.and()
		
		
		// logout & cors 
		.logout().logoutSuccessHandler(new Logout())
		.permitAll()
		.and()
		.cors();
	}

	@Override
    public void configure(WebSecurity web) throws Exception {
      web
        .ignoring()
           .antMatchers("/img/**")    
           .antMatchers("/css/**")      
           .antMatchers("/js/**");     
    }
	
	
	@Override
	public void addCorsMappings(CorsRegistry registry)
	{
		registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS",  "HEAD", "TRACE", "CONNECT")
        .allowedHeaders("*")
        .exposedHeaders("Authorization","Content-Disposition");  
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

}