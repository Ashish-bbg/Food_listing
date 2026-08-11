package com.food.dto.response;

import java.util.UUID;

import com.food.enums.UserRole;
import com.food.enums.VerificationStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

	private UUID id;
	
	private String name;
	
	private String email;
	
	private String phone;
	
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	@Enumerated(EnumType.STRING)
	private VerificationStatus verificationStatus;
}
