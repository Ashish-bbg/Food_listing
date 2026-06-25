package com.food.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodClaimRequest {
	
	@NotNull(message = "Food Id cannot be null")
	private UUID foodId;
	
	@NotNull(message = "User Id cannot be null")
	private UUID userId;
	
	@NotNull(message = "Quantity field is required, it cannot be empty")
	@Min(value = 1, message = "Quantity must be atleast 1")
	private Integer quantity;
	

	
}
