package com.food.service;

import com.food.dto.CreateFoodListingRequest;
import com.food.entity.FoodListing;

public interface FoodListingService {

	FoodListing createFoodListing(CreateFoodListingRequest request);
	
}
