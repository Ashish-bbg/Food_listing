package com.food.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.dto.request.CreateUserRequest;
import com.food.dto.request.LoginRequest;
import com.food.dto.response.LoginResponse;
import com.food.dto.response.LoginResult;
import com.food.dto.response.UserResponse;
import com.food.service.AuthService;
import com.food.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	private final UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(
			@Valid @RequestBody LoginRequest request,
			HttpServletResponse response
			
			) {
		
		LoginResult result = authService.login(request);
		
		Cookie refreshCookie = new Cookie("refreshToken", result.getRefreshToken());
		
		refreshCookie.setHttpOnly(true);
		refreshCookie.setSecure(false); // in production true for localhost testing false
		refreshCookie.setPath("/auth/refresh");
		refreshCookie.setMaxAge(30 * 24 * 60 * 60);
		
		response.addCookie(refreshCookie);
		
		return ResponseEntity.ok(
				new LoginResponse(result.getAccessToken())
				);
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequest request) {
		UserResponse userResponse = userService.createUser(request);
		
		return ResponseEntity.ok("User created Successfully " + userResponse);
	}

	@PostMapping("/refresh")
	public ResponseEntity<LoginResponse> refreshToken(
			@CookieValue(name = "refreshToken", required=false) String refreshToken,
			HttpServletResponse response) {
		
		LoginResult result = authService.refreshToken(refreshToken);
		
		Cookie refreshCookie = new Cookie("refreshToken", result.getRefreshToken());	
		
		refreshCookie.setHttpOnly(true);
		refreshCookie.setSecure(false); // local HTTP only
		refreshCookie.setPath("/auth/refresh");
		refreshCookie.setMaxAge(30 * 24 * 60 * 60);
		
		response.addCookie(refreshCookie);
		
		return ResponseEntity.ok(
				new LoginResponse(result.getAccessToken())
				);
		
	}
	
	@PostMapping("/logout")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> logout(HttpServletResponse response){
		
		authService.logout();
		
		Cookie refreshCookie = new Cookie("refreshToken", null);
		
		refreshCookie.setHttpOnly(true);
		refreshCookie.setSecure(false);
		refreshCookie.setPath("/auth/refresh");
		refreshCookie.setMaxAge(0);
		
		response.addCookie(refreshCookie);
		
		return ResponseEntity.ok("Logout successfully");
		
	}

}
