package com.food.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNgoVerification {

	@NotBlank(message = "Ngo name cannot be empty")
	private String ngoName;
	
	@NotBlank(message = "Description is Required")
	@Size(max = 1000)
	private String description;
	
	@NotBlank(message = "Address is Required")
	private String address;
	
	@NotBlank(message = "City is required")
	private String city;
	
	private String website;
	
}
