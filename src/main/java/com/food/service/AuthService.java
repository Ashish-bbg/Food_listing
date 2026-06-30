package com.food.service;

import com.food.dto.LoginRequest;
import com.food.dto.LoginResponse;

public interface AuthService {
	
	public LoginResponse login(LoginRequest request);

}
