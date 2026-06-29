package com.food.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.food.dto.CreateUserRequest;
import com.food.entity.User;
import com.food.enums.VerificationStatus;
import com.food.exception.EmailAlreadyExistsException;
import com.food.exception.PhoneAlreadyExistsException;
import com.food.repository.UserRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Builder
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public User createUser(CreateUserRequest request) {
		boolean emailExist = userRepository.existsByEmail(request.getEmail()); 
		boolean phoneExist = userRepository.existsByPhone(request.getPhone());
		
		if(emailExist) {
			 throw new EmailAlreadyExistsException("Email Already exists");
		}
		if(phoneExist) {
			throw new PhoneAlreadyExistsException("Phone Already exists");
		}
		
//		User user = new User();
//		
//		user.setEmail(request.getEmail());
//		user.setName(request.getName());
//		user.setPhone(request.getPhone());
//		user.setRole(request.getRole());
//		user.setVerificationStatus(VerificationStatus.PENDING);

		
		User user = User.builder()
				.name(request.getName())
				.email(request.getEmail())
				.phone(request.getPhone())
				.role(request.getRole())
				.password(passwordEncoder.encode(request.getPassword()))
				.verificationStatus(VerificationStatus.PENDING)
				.build();
		
		return userRepository.save(user);

	}

}
