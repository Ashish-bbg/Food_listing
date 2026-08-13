package com.food.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.food.dto.request.FoodListingRequest;
import com.food.dto.response.FoodListingResponse;
import com.food.entity.FoodListing;
import com.food.enums.FoodType;

public interface FoodListingService {

	FoodListing createFoodListing(FoodListingRequest request);
	
	Page<FoodListingResponse> getAllFoodListing(
			FoodType foodType,
			String city,
			Double maxCost,
			Double minCost,
			Integer minQuantity,
			Double latitude,
			Double longitude,
			Double radiusKm,
			Pageable pageable);
	
	FoodListingResponse getFoodById(UUID id);
	
	String deleteFoodById(UUID id);
	
	FoodListingResponse updateFoodById(UUID id, FoodListingRequest request);
}
