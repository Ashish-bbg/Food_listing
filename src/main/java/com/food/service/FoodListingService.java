package com.food.service;

import java.util.List;
import java.util.UUID;

import com.food.dto.FoodListingRequest;
import com.food.dto.response.FoodListingResponse;
import com.food.entity.FoodListing;

public interface FoodListingService {

	FoodListing createFoodListing(FoodListingRequest request);
	
	List<FoodListingResponse> getAllFoodListing();
	
	FoodListingResponse getFoodById(UUID id);
	
	String deleteFoodById(UUID id);
	
	FoodListingResponse updateFoodById(UUID id, FoodListingRequest request);
}
