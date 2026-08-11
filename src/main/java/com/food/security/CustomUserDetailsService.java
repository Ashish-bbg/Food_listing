package com.food.security;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.food.entity.User;
import com.food.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(username)
				.orElseThrow(()-> new UsernameNotFoundException("User not found: "+username));
		
		return new CustomUserDetails(user);

	}
	

	public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
		
		User user = userRepository.findById(UUID.fromString(userId))
				.orElseThrow(()-> new UsernameNotFoundException("User not found: " + userId));
		
		return new CustomUserDetails(user);
		
	}
	
}
