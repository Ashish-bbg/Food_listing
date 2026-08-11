package com.food.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.dto.request.CreateUserRequest;
import com.food.dto.request.LoginRequest;
import com.food.dto.request.LoginResponse;
import com.food.dto.response.UserResponse;
import com.food.entity.User;
import com.food.service.AuthService;
import com.food.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	private final UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
		
		LoginResponse token = authService.login(request);
		
		return ResponseEntity.ok(token);
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequest request) {
		UserResponse userResponse = userService.createUser(request);
		
		return ResponseEntity.ok("User created Successfully " + userResponse);
	}


}
