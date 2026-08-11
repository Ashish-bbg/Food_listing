package com.food.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.food.dto.request.LoginRequest;
import com.food.dto.request.LoginResponse;
import com.food.entity.User;
import com.food.exception.InvalidCredentialsException;
import com.food.repository.UserRepository;
import com.food.security.CustomUserDetails;
import com.food.security.JwtService;
import com.food.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	
	
	private final JwtService jwtService;
	
	private final AuthenticationManager authenticationManager;
	
	@Override
	public LoginResponse login(LoginRequest request) {
				
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword())
				);
		
		 CustomUserDetails details =  (CustomUserDetails) authentication.getPrincipal();
		
		String token = jwtService.generateToken(details.getUser());
		
		return new LoginResponse(token);
		
	}

}
