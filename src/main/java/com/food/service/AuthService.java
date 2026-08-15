package com.food.service;

import com.food.dto.request.LoginRequest;
import com.food.dto.request.RefreshTokenRequest;
import com.food.dto.response.LoginResponse;

public interface AuthService {
	
	public LoginResponse login(LoginRequest request);
	
	public LoginResponse refreshToken(RefreshTokenRequest request);
	
	public void  logout();

}
