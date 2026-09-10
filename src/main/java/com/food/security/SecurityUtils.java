package com.food.security;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.food.entity.User;

public class SecurityUtils {
	
	public static User getCurrentUser() {
		Authentication authentication =
				SecurityContextHolder.getContext().getAuthentication();
		
		CustomUserDetails customerUserDetails =
				(CustomUserDetails) authentication.getPrincipal();
		
		return customerUserDetails.getUser();
	}
	
	public static UUID getCurrentUserId() {
		return getCurrentUser().getId();
	}

}
