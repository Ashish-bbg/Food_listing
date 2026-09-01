package com.food.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.food.enums.RoleRequestStatus;
import com.food.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminRoleRequestResponse {
	
	private UUID id;
	
	private UUID userId;
	
	private String userName;
	
	private String email;
	
	private UserRole requestedRole;
	
	private RoleRequestStatus status;
	
	private LocalDateTime createdAt;

}
