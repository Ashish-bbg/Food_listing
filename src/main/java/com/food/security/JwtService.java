package com.food.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.food.FoodApplication;
import com.food.entity.User;
import com.food.exception.InvalidTokenException;
import com.food.exception.TokenExpiredException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private long expiration;
	
	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	
	public String generateToken(User user) {
		
		return Jwts.builder()
		.subject(user.getId().toString())
		.claim("role", user.getRole().name())
		.issuedAt(new Date())
		.expiration(new Date(System.currentTimeMillis() + expiration))
		.signWith(getSigningKey())
		.compact();
	}
	
	private Claims extractAllClaims(String token) {
		
		try {
			return Jwts.parser()
			.verifyWith(getSigningKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
		} 
		catch(ExpiredJwtException ex) {
			throw new TokenExpiredException("JWT has expired");
		}
		catch(JwtException ex) {
			throw new InvalidTokenException("Invalid JWT token");
		}
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> resolver) {
		
		Claims claims = extractAllClaims(token);
		
		return resolver.apply(claims);
		
	}
	
//	public String extractEmail(String token) {
//		return extractClaim(token, Claims::getSubject);
//	}
	
	public String extractUserId(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	
	public boolean isValidToken(String userId, UserDetails userDetails) {
		
		if(!userId.equals(userDetails.getUsername())) {
			throw new InvalidTokenException("Token does not belong to the authenticated user");
		}
						
		
		return true;
	}

}
