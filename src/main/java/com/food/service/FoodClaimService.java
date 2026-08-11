package com.food.service;

import java.util.List;
import java.util.UUID;

import com.food.dto.request.FoodClaimRequest;
import com.food.dto.response.FoodClaimResponse;
import com.food.entity.FoodClaim;

public interface FoodClaimService {

	public FoodClaimResponse claimFood(FoodClaimRequest request);
	
	public void confirmFoodClaim(UUID id);
	
	public void cancelExpiredReservation();
	
	public List<FoodClaimResponse> getMyClaims();
	
	public FoodClaimResponse getClaimById(UUID id);
	
	public FoodClaimResponse cancelFoodClaimById(UUID id);
	
	public List<FoodClaimResponse> getClaimsForMyFoodListings();
	
}
