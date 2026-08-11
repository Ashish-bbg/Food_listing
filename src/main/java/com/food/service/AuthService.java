package com.food.service;

import com.food.dto.request.LoginRequest;
import com.food.dto.request.LoginResponse;

public interface AuthService {
	
	public LoginResponse login(LoginRequest request);

}
