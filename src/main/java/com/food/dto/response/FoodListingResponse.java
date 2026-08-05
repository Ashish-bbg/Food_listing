package com.food.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.food.enums.FoodStatus;
import com.food.enums.FoodType;

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
public class FoodListingResponse {

	private UUID id;
	
	private String foodName;
	
	private FoodType foodType;
	
	private Integer quantity;
	
	private Double cost;
	
	private String city;
	
	private Double latitude;
	
	private Double longitude;
	
	private LocalDateTime expiryTime;
	
	private FoodStatus status;
	
	private UUID hostId;
	
	private LocalDateTime createdAt;
	
}
