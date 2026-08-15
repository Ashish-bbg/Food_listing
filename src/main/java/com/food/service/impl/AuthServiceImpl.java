package com.food.service.impl;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.food.dto.request.LoginRequest;
import com.food.dto.request.RefreshTokenRequest;
import com.food.dto.response.LoginResponse;
import com.food.dto.response.LoginResult;
import com.food.entity.RefreshToken;
import com.food.entity.User;
import com.food.exception.UserNotFoundException;
import com.food.repository.UserRepository;
import com.food.security.CustomUserDetails;
import com.food.security.CustomUserDetailsService;
import com.food.security.JwtService;
import com.food.service.AuthService;
import com.food.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

	
	private final JwtService jwtService;
	
	private final AuthenticationManager authenticationManager;
	
	private final RefreshTokenService refreshTokenService;
	
	private final UserRepository userRepository;


	
	@Override
	public LoginResult login(LoginRequest request) {
				
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword())
				);
		
		CustomUserDetails details =  (CustomUserDetails) authentication.getPrincipal();
		
		User user = details.getUser();
		
		String accessToken = jwtService.generateToken(user);
		
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
				
		return LoginResult.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken.getToken())
				.build();
		
	}

	@Override
	public LoginResult refreshToken(String token) {
		
		RefreshToken oldRefreshToken = refreshTokenService.verifyRefreshToken(token);
		
		User user = userRepository.findById(oldRefreshToken.getUserId())
			.orElseThrow(()-> new UserNotFoundException("User not found"));
		
		String accessToken = jwtService.generateToken(user);
		
		
		RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
		
		return LoginResult.builder()
				.accessToken(accessToken)
				.refreshToken(newRefreshToken.getToken())
				.build();
	}

	@Override
	public void logout() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		CustomUserDetails  customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		
		UUID userId = customUserDetails.getUser().getId();
		
		refreshTokenService.deleteRefreshToken(userId);
		
		SecurityContextHolder.clearContext();
		
	}

}
