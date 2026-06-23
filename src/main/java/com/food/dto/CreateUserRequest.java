package com.food.dto;

import com.food.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

	@NotBlank(message = "Name cannot be empty")
	private String name;
	
	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Please enter valid email format")
	private String email;
	
	
	@NotBlank(message = "Phone number cannot be empty")
	private String phone;
	
	@NotNull(message = "Role cannot be empty")
	private UserRole role;
	
}
