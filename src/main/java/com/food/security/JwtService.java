package com.food.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.food.entity.User;

import io.jsonwebtoken.Claims;
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
		.subject(user.getEmail())
		.claim("userID", user.getId().toString())
		.claim("role", user.getRole().name())
		.issuedAt(new Date())
		.expiration(new Date(System.currentTimeMillis() + expiration))
		.signWith(getSigningKey())
		.compact();
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts.parser()
		.verifyWith(getSigningKey())
		.build()
		.parseSignedClaims(token)
		.getPayload();
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> resolver) {
		
		Claims claims = extractAllClaims(token);
		
		return resolver.apply(claims);
		
	}
	
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}

}
