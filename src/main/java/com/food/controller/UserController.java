package com.food.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.dto.request.UserRequest;
import com.food.dto.response.UserResponse;
import com.food.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/me")
	public ResponseEntity<UserResponse> getUser() {
		
		return ResponseEntity.ok(userService.getCurrUser());
		
	}
	
	@PutMapping("/me")
	public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest request){
		return ResponseEntity.ok(userService.updateCurrUser(request));
	}

	

	
}
