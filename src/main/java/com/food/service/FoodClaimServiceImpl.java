package com.food.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.food.dto.FoodClaimRequest;
import com.food.entity.FoodClaim;
import com.food.entity.FoodListing;
import com.food.enums.FoodClaimStatus;
import com.food.enums.FoodStatus;
import com.food.exception.FoodClaimNotFoundException;
import com.food.exception.FoodNotFoundException;
import com.food.exception.FoodUnavailableException;
import com.food.exception.InvalidClaimStateException;
import com.food.exception.UserNotFoundException;
import com.food.repository.FoodClaimRepository;
import com.food.repository.FoodListingRepository;
import com.food.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
//@Transactional
@RequiredArgsConstructor
public class FoodClaimServiceImpl implements FoodClaimService {

    private final FoodClaimRepository foodClaimRepository;
	
	private final UserRepository userRepository;
	
	private final FoodListingRepository foodListingRepository;

	@Override
	@Transactional
	public FoodClaim claimFood(FoodClaimRequest request) {
		userRepository.findById(request.getUserId())
			.orElseThrow(()-> new UserNotFoundException("User not found"));
		
		foodListingRepository.findById(request.getFoodId())
			.orElseThrow(()-> new FoodNotFoundException("Food not found"));
	
		int foodUpdate = foodListingRepository.reserveFood(request.getFoodId(), request.getQuantity());
		
		if(foodUpdate == 0) {
			throw new FoodUnavailableException("Food is unavailable or insufficient quantity");
		}
		
		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			
//			e.printStackTrace();
//		}
//		
		
		
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
			.orElseThrow(()-> new FoodClaimNotFoundException("Food claim not found"));
		
		if(foodClaim.getStatus() != FoodClaimStatus.RESERVED) {
			throw new InvalidClaimStateException("Sorry food is already claimed or cancelled");
		}
		
		foodClaim.setStatus(FoodClaimStatus.CLAIMED);
		
		
		foodClaim.setClaimedAt(LocalDateTime.now());
		
		foodClaimRepository.save(foodClaim);
		
	}

	@Override
	@Transactional
	public void cancelExpiredReservation() {
				
		List<FoodClaim> expiredClaims =  foodClaimRepository
				.findByStatusAndReservedUntilBefore(FoodClaimStatus.RESERVED,
						LocalDateTime.now());
		
		List<FoodClaim> foodClaims = new ArrayList<>();
		List<FoodListing> foodListings = new ArrayList<>();
		LocalDateTime now = LocalDateTime.now();
		
		for(FoodClaim claim: expiredClaims) {
						
			try {
				FoodListing foodListing = foodListingRepository.findById(claim.getFoodId())
						.orElseThrow(()-> new FoodNotFoundException("Food Not found"));
				
			
				claim.setStatus(FoodClaimStatus.CANCELLED);
				claim.setCancelledAt(now);
				foodClaims.add(claim);
				
				
				Integer restoredQuantity = claim.getQuantity() + foodListing.getQuantity();
				
				foodListing.setQuantity(restoredQuantity);
				foodListing.setStatus(FoodStatus.AVAILABLE);
				
				foodListings.add(foodListing);
				
				} catch (Exception e) {
					System.out.printf("Failed to expire reservation {}", claim.getId() + " "+ e.getMessage());
					
				}
			
		}
		
		if(!foodClaims.isEmpty()) {
			foodClaimRepository.saveAll(foodClaims);			
		}
		
		if(!foodListings.isEmpty()) {
			foodListingRepository.saveAll(foodListings);			
		}
		
		
	}

}
