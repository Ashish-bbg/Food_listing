package com.food.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.food.enums.FoodStatus;
import com.food.enums.FoodType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
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
public class CreateFoodListingRequest {
	
	@NotBlank(message = "Food name cannot be empty")
	private String foodName;
	
	@NotNull(message = "Food Type cannot be empty")
	private FoodType foodType;
	

	@NotNull(message = "Quantity cannot is empty")
	@Min(value = 1, message = "Quantity must be at least 1")
	private Integer quantity;
	
	@NotNull(message = "Cost cannot be empty")
	@DecimalMin(value="0.0")
	private Double cost;
	
	@NotBlank(message="City cannot be empty")
	private String city;
	
	private Double latitude;
	
	private Double longitude;
	
	
	@NotNull(message="Expiry Time cannot be empty")
	@Future(message = "Expiry Time must be in the future")
	private LocalDateTime expiryTime;
	
	@NotNull(message="HostId cannot be empty")
	private UUID hostId;
}
