package com.food.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.food.dto.request.FoodListingRequest;
import com.food.dto.response.FoodListingResponse;
import com.food.entity.FoodListing;
import com.food.enums.FoodStatus;
import com.food.exception.FoodListingNotFoundException;
import com.food.repository.FoodListingRepository;
import com.food.security.CustomUserDetails;
import com.food.service.FoodListingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodListingServiceImpl implements FoodListingService {

	private final FoodListingRepository foodListingRepository;

//	Post req create food req
	@Override
	public FoodListing createFoodListing(FoodListingRequest request) {
				
		UUID hostId = getCurrentUserId();
		
		
		FoodListing foodListing = FoodListing.builder()
				.city(request.getCity())
				.cost(request.getCost())
				.foodName(request.getFoodName())
				.foodType(request.getFoodType())
				.latitude(request.getLatitude())
				.longitude(request.getLongitude())
				.hostId(hostId)
				.quantity(request.getQuantity())
				.status(FoodStatus.AVAILABLE)
				.expiryTime(request.getExpiryTime())
				.build();
		
		return foodListingRepository.save(foodListing);
	}

//	Get Req All foods
	@Override
	public List<FoodListingResponse> getAllFoodListing() {
		
		List<FoodListing> foodLists = foodListingRepository.findAll();
		
		List<FoodListingResponse> foodListingResponse = new ArrayList<>();
		
		for(FoodListing foodListing: foodLists) {
			if(foodListing.getStatus()!=FoodStatus.EXPIRED)
				foodListingResponse.add(mapToResponse(foodListing));			
		}
		
		return foodListingResponse;
	}

//  Get Req Get food by ID
	@Override
	public FoodListingResponse getFoodById(UUID id) {
		FoodListing foodListing= foodListingRepository
				.findById(id)
				.orElseThrow(()->
				 new FoodListingNotFoundException("Food listing not found"));	
		
        return mapToResponse(foodListing);
	}

//  Delete Req by id
	@Override
	public String deleteFoodById(UUID id) {
						
		FoodListing foodListing = getOwnedFoodListing(id);
		
		foodListingRepository.delete(foodListing);
		return "Food listing deleted successfully";
		
	}
	
//	Update Req by id
	
	@Override
	public FoodListingResponse updateFoodById(UUID id, FoodListingRequest request) {
		
		FoodListing foodListing = getOwnedFoodListing(id);
		
		// update field
		 foodListing.setFoodName(request.getFoodName());
		 foodListing.setFoodType(request.getFoodType());
		 foodListing.setQuantity(request.getQuantity());
		 foodListing.setCost(request.getCost());
		 foodListing.setCity(request.getCity());
		 foodListing.setLatitude(request.getLatitude());
		 foodListing.setLongitude(request.getLongitude());
		 foodListing.setExpiryTime(request.getExpiryTime());
		
		return mapToResponse(foodListingRepository.save(foodListing));
	}
	
//	Helper methods
	private FoodListingResponse mapToResponse(FoodListing foodListing) {
		return FoodListingResponse.builder() 
				.id(foodListing.getId())
				.foodName(foodListing.getFoodName())
				.foodType(foodListing.getFoodType())
				.quantity(foodListing.getQuantity())
				.cost(foodListing.getCost())
				.city(foodListing.getCity())
				.latitude(foodListing.getLatitude())
				.longitude(foodListing.getLongitude())
				.expiryTime(foodListing.getExpiryTime())
				.status(foodListing.getStatus())
				.hostId(foodListing.getHostId())
				.createdAt(foodListing.getCreatedAt())
				.build();
	}
	
	private UUID getCurrentUserId() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		
		return customUserDetails.getUser().getId();
		
	}
	
	private FoodListing getOwnedFoodListing(UUID id) {
		
		FoodListing foodListing = foodListingRepository.findById(id)
				.orElseThrow(()-> 
				new FoodListingNotFoundException("Food listing not found"));
		
		if(!foodListing.getHostId().equals(getCurrentUserId())) {
			throw new AccessDeniedException("You are not authorized to modify this food listing.");
		} 
		
		return foodListing;
	}
}
