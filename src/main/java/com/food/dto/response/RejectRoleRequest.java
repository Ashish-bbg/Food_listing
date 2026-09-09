package com.food.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectRoleRequest {
	
	@NotBlank
	private String rejectionReason;
	
}
