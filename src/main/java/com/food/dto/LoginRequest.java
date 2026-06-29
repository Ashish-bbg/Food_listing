package com.food.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

	@NotEmpty(message = "Email cannot be empty")
	@Email(message = "Please enter correct email foramt")
	private String email;
	
	@NotEmpty(message = "Password is required")
	private String password;
	
}
