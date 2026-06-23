package com.food.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.dto.CreateUserRequest;
import com.food.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	
	@PostMapping
	public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequest request) {
		userService.createUser(request);
		
		return ResponseEntity.ok("User created Successfully");
	}

}
