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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequestResponse {

	private UUID id;
	
	private UserRole requestedRole;
	
	private RoleRequestStatus status;
		
	private LocalDateTime createdAt;
	
}
