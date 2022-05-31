package com.eleodoro.dev.security;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.eleodoro.dev.config.WebConfig;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Jwt Util
 * @author matheusses
 */
@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;


	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}


	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}


	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}



	private Claims getAllClaimsFromToken(String token) {

		return Jwts.parser().setSigningKey(WebConfig.SECRET).parseClaimsJws(token).getBody();
	}


	public Boolean isInvalidToken(String token)
	{
		if(token==null)
			return true;
		
		DecodedJWT jwt = JWT.decode(token);
		return jwt.getExpiresAt().before(new Date());
	}


	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}


	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}


	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + WebConfig.EXPIRATION))
				.signWith(SignatureAlgorithm.HS256, WebConfig.SECRET).compact();
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isInvalidToken(token) || ignoreTokenExpiration(token));
	}


	public Boolean validateToken(String token, UserDatailsImpl userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isInvalidToken(token));
	}
}