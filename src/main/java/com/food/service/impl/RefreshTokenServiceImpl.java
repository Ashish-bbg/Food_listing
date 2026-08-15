package com.food.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.food.entity.RefreshToken;
import com.food.exception.InvalidTokenException;
import com.food.repository.RefreshTokenRepository;
import com.food.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
	
	
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	@Transactional
	public RefreshToken createRefreshToken(UUID userID) {
		
		refreshTokenRepository.deleteByUserId(userID);
		
		RefreshToken refreshToken = RefreshToken.builder()
		.token(generateRefreshToken())
		.userId(userID)
		.expiresAt(LocalDateTime.now().plusDays(30))
		.build();
		
		return refreshTokenRepository.save(refreshToken);
		
	}
	
	@Override
	public RefreshToken verifyRefreshToken(String token) {
		
		RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
		.orElseThrow(()-> new InvalidTokenException("Invalid refresh token"));
		
		if(refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			refreshTokenRepository.delete(refreshToken);
			
			throw new InvalidTokenException("Refresh token has expired");
		}
		
		return refreshToken;
	}


	@Override
	@Transactional
	public void deleteRefreshToken(UUID userID) {
		
		refreshTokenRepository.deleteByUserId(userID);
		
	}
	
	
	private String generateRefreshToken() {
		
		byte[] randomBytes =new byte[64];
		
		new SecureRandom().nextBytes(randomBytes);
		
		return Base64.getUrlEncoder()
				.withoutPadding()
				.encodeToString(randomBytes);
		
	}

	

}
