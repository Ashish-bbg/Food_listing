package com.food.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.food.dto.LoginRequest;
import com.food.entity.User;
import com.food.exception.InvalidCredentialsException;
import com.food.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public void login(LoginRequest request) {
		
		User user = userRepository.findByEmail(request.getEmail())
		.orElseThrow(()-> new InvalidCredentialsException("Invalid email or password"));
		
		boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
		
		if(!matches) {
			throw new InvalidCredentialsException("Invalid email or password");
		}
		
	}

}
