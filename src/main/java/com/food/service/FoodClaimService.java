package com.food.service;

import java.util.UUID;

import com.food.dto.FoodClaimRequest;
import com.food.entity.FoodClaim;

public interface FoodClaimService {

	public FoodClaim claimFood(FoodClaimRequest request);
	
	public void confirmFoodClaim(UUID id);
	
	public void cancelExpiredReservation();
	
}
