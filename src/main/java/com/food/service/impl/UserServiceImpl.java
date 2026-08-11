package com.food.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.food.dto.request.CreateUserRequest;
import com.food.dto.request.UserRequest;
import com.food.dto.response.UserResponse;
import com.food.entity.User;
import com.food.enums.VerificationStatus;
import com.food.exception.EmailAlreadyExistsException;
import com.food.exception.PhoneAlreadyExistsException;
import com.food.exception.UserNotFoundException;
import com.food.repository.UserRepository;
import com.food.security.CustomUserDetails;
import com.food.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserResponse createUser(CreateUserRequest request) {
		boolean emailExist = userRepository.existsByEmail(request.getEmail()); 
		boolean phoneExist = userRepository.existsByPhone(request.getPhone());
		
		if(emailExist) {
			 throw new EmailAlreadyExistsException("Email Already exists");
		}
		if(phoneExist) {
			throw new PhoneAlreadyExistsException("Phone Already exists");
		}
				
		User user = User.builder()
				.name(request.getName())
				.email(request.getEmail())
				.phone(request.getPhone())
				.role(request.getRole())
				.password(passwordEncoder.encode(request.getPassword()))
				.verificationStatus(VerificationStatus.PENDING)
				.build();
		
		return mapToUserResponse(userRepository.save(user));

	}

	@Override
	public UserResponse getCurrUser() {

	 	User user = getCurrentUser();
	 	
		return mapToUserResponse(user);
	}

	@Override
	public UserResponse updateCurrUser(UserRequest request) {
				
	 	User user = userRepository.findById(getCurrentUser().getId())
	 		.orElseThrow(()-> new UserNotFoundException("User not found") );
	 	
	 	if(userRepository.existsByEmailAndIdNot(request.getEmail(), user.getId())) {
	 		throw new EmailAlreadyExistsException("Email already exists");
	 	}
	 	
	 	if(userRepository.existsByPhoneAndIdNot(request.getPhone(), user.getId())) {
	 		throw new PhoneAlreadyExistsException("Phone already exists");
	 	}
	 	
	 	user.setEmail(request.getEmail());
	 	user.setName(request.getName());
	 	user.setPhone(request.getPhone());
		
	 	User userRepo = userRepository.save(user);
		
	 	return mapToUserResponse(userRepo);

	}
	
	
	private UserResponse mapToUserResponse(User user) {
		
		return UserResponse.builder()
		 		.id(user.getId())
		 		.name(user.getName())
		 		.email(user.getEmail())
		 		.phone(user.getPhone())
		 		.role(user.getRole())
			 	.verificationStatus(user.getVerificationStatus())
		 		.build();
		
	}
	
	private User getCurrentUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		
		return customUserDetails.getUser();
	}

}
