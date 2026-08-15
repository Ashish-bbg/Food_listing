package com.food.service;

import com.food.dto.request.LoginRequest;
import com.food.dto.request.RefreshTokenRequest;
import com.food.dto.response.LoginResponse;
import com.food.dto.response.LoginResult;

public interface AuthService {
	
	public LoginResult login(LoginRequest request);
	
	public LoginResult refreshToken(String refreshToken);
	
	public void  logout();

}
