package com.food.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.food.dto.FoodClaimRequest;
import com.food.entity.FoodClaim;
import com.food.entity.FoodListing;
import com.food.enums.FoodClaimStatus;
import com.food.enums.FoodStatus;
import com.food.exception.FoodNotFoundException;
import com.food.exception.FoodUnavailableException;
import com.food.exception.NotEnoughFoodAvailableException;
import com.food.exception.UserNotFoundException;
import com.food.repository.FoodClaimRepository;
import com.food.repository.FoodListingRepository;
import com.food.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FoodClaimServiceImpl implements FoodClaimService {

    private final FoodClaimRepository foodClaimRepository;
	
	private final UserRepository userRepository;
	
	private final FoodListingRepository foodListingRepository;

	@Override
	public FoodClaim claimFood(FoodClaimRequest request) {
		userRepository.findById(request.getUserId())
			.orElseThrow(()-> new UserNotFoundException("User not found"));
		
		FoodListing foodListing = foodListingRepository.findById(request.getFoodId())
			.orElseThrow(()-> new FoodNotFoundException("Food not found"));
		
		if(foodListing.getStatus() != FoodStatus.AVAILABLE) {
			throw new FoodUnavailableException("Food is not available for claiming");
		}
		
		// checking req quantity and available quantity
		Integer availableFoodQuantity = foodListing.getQuantity();
		Integer reqFoodQuantity = request.getQuantity();
		
		if(request.getQuantity() > availableFoodQuantity) {
			throw new NotEnoughFoodAvailableException("Requested quantity exceeds available quantity");
		}
		
		// checking if both are same if same then making food listing as RESERVED
		if(availableFoodQuantity.equals(reqFoodQuantity)) {
			foodListing.setStatus(FoodStatus.RESERVED);
		}
		
		foodListing.setQuantity(availableFoodQuantity - reqFoodQuantity);
		
		foodListingRepository.save(foodListing);
		
		
		FoodClaim foodClaim = FoodClaim.builder()
				.foodId(request.getFoodId())
				.userId(request.getUserId())
				.quantity(request.getQuantity())
				.build();
		
		return foodClaimRepository.save(foodClaim);

	}

	@Override
	public void confirmFoodClaim(UUID id) {
		
		FoodClaim foodClaim = foodClaimRepository.findById(id)
			.orElseThrow(()-> new FoodNotFoundException("Food not found"));
		
		
		
		foodClaim.setStatus(FoodClaimStatus.CLAIMED);
		foodClaim.setClaimedAt(LocalDateTime.now());
		
		foodClaimRepository.save(foodClaim);
		
	}

}
