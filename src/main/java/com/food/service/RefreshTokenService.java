package com.food.service;

import java.util.UUID;

import com.food.entity.RefreshToken;

public interface RefreshTokenService {

	RefreshToken createRefreshToken(UUID userID);
	
	RefreshToken verifyRefreshToken(String token);
	
	void deleteRefreshToken(UUID userID);
	
}
