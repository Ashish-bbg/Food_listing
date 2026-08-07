package com.food.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.food.dto.FoodClaimRequest;
import com.food.dto.response.FoodClaimResponse;
import com.food.entity.FoodClaim;
import com.food.entity.FoodListing;
import com.food.enums.FoodClaimStatus;
import com.food.enums.FoodStatus;
import com.food.exception.FoodClaimNotFoundException;
import com.food.exception.FoodListingNotFoundException;
import com.food.exception.FoodNotFoundException;
import com.food.exception.FoodUnavailableException;
import com.food.exception.InvalidClaimStateException;
import com.food.repository.FoodClaimRepository;
import com.food.repository.FoodListingRepository;
import com.food.security.CustomUserDetails;
import com.food.service.FoodClaimService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
//@Transactional
@RequiredArgsConstructor
@Slf4j
public class FoodClaimServiceImpl implements FoodClaimService {
	

    private final FoodClaimRepository foodClaimRepository;
	
	private final FoodListingRepository foodListingRepository;

	@Override
	@Transactional
	public FoodClaimResponse claimFood(FoodClaimRequest request) {
		
//		foodListingRepository.findById(request.getFoodId())
//			.orElseThrow(()-> new FoodNotFoundException("Food not found"));
//	
		int foodUpdate = foodListingRepository.reserveFood(request.getFoodId(), request.getQuantity());
		
		if(foodUpdate == 0) {
			throw new FoodUnavailableException("Food is unavailable or insufficient quantity");
		}	
		
		FoodClaim foodClaim = FoodClaim.builder()
				.foodId(request.getFoodId())
				.userId(getCurrentUserId())
				.quantity(request.getQuantity())
				.build();
			
		FoodClaim savedFoodClaim = foodClaimRepository.save(foodClaim);
		
		return mapToResponse(savedFoodClaim);

	}

	@Override
	public void confirmFoodClaim(UUID id) {
			
		FoodClaim foodClaim = getOwnedFoodClaim(id);
			
		if(foodClaim.getStatus() != FoodClaimStatus.RESERVED) {
			throw new InvalidClaimStateException("Sorry food is already claimed or cancelled");
		}
		
		if(foodClaim.getReservedUntil().isBefore(LocalDateTime.now())) {
			throw new InvalidClaimStateException("Reservation has expired, pls reserve again");
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
			} catch(Exception e) {
				  log.error("Failed to expire claim " + claim.getId() + ": " + e.getMessage()
			        );
			}
			
		} 
				
		if(!foodClaims.isEmpty()) {
			foodClaimRepository.saveAll(foodClaims);			
		}
		
		if(!foodListings.isEmpty()) {
			foodListingRepository.saveAll(foodListings);			
		}
		
	}
	
	@Override
	public List<FoodClaimResponse> getMyClaims() {
		
		List<FoodClaim> claims = foodClaimRepository.findByUserId(getCurrentUserId());
		
		List<FoodClaimResponse> foodClaimResponses = new ArrayList<>();
		
		for(FoodClaim claim: claims) {
			foodClaimResponses.add(mapToResponse(claim));
		}
		
		return foodClaimResponses;
	}
	

	@Override
	public FoodClaimResponse getClaimById(UUID id) {
		
		FoodClaim foodClaim = getOwnedFoodClaim(id);
		
		return mapToResponse(foodClaim);
	}
	
	
	@Override
	@Transactional
	public FoodClaimResponse cancelFoodClaimById(UUID id) {
		
		FoodClaim foodClaim = getOwnedFoodClaim(id);
		
		if(foodClaim.getReservedUntil().isBefore(LocalDateTime.now())) {
			throw new InvalidClaimStateException("Reservation has already expired");
		}
		
		if(foodClaim.getStatus()!=FoodClaimStatus.RESERVED) {
			 throw new InvalidClaimStateException("Only reserved claims can be cancelled");
		}
		
		foodClaim.setStatus(FoodClaimStatus.CANCELLED);
		foodClaim.setCancelledAt(LocalDateTime.now());
		
		FoodListing foodListing = foodListingRepository.findById(foodClaim.getFoodId())
			.orElseThrow(()-> new FoodListingNotFoundException("Food Listing not found"));
		
		Integer updatedQuantity = foodListing.getQuantity() + foodClaim.getQuantity();
		
		foodListing.setQuantity(updatedQuantity);
		foodListing.setStatus(FoodStatus.AVAILABLE);
		
		FoodClaim claim = foodClaimRepository.save(foodClaim);
		
		foodListingRepository.save(foodListing);
		
		return mapToResponse(claim);
	}
	
	
//	Get the current user
	private UUID getCurrentUserId() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
		
		return customUserDetails.getUser().getId();
		
	}
	
//	convert Claim to claim response
	private FoodClaimResponse mapToResponse(FoodClaim foodClaim) {
		
		return FoodClaimResponse.builder()
		.id(foodClaim.getId())
		.foodId(foodClaim.getFoodId())
		.quantity(foodClaim.getQuantity())
		.status(foodClaim.getStatus())
		.reservedAt(foodClaim.getReservedAt())
		.reservedUntil(foodClaim.getReservedUntil())
		.claimedAt(foodClaim.getClaimedAt())
		.cancelledAt(foodClaim.getCancelledAt())
		.build();
		
	}


// check if user is authorized to get this food
	private FoodClaim getOwnedFoodClaim(UUID id) {
		
		FoodClaim foodClaim = foodClaimRepository.findById(id)
				.orElseThrow(()-> new FoodClaimNotFoundException("Claim not found"));
		
		if(!foodClaim.getUserId().equals(getCurrentUserId())) {
			throw new AccessDeniedException("You are not authorized to do this operation");
		} 
		
		return foodClaim;
		
	}

	
}
