package com.food.service;

import org.springframework.stereotype.Service;

import com.food.dto.CreateFoodListingRequest;
import com.food.entity.FoodListing;
import com.food.enums.FoodStatus;
import com.food.exception.UserNotFoundException;
import com.food.repository.FoodListingRepository;
import com.food.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodListingServiceImpl implements FoodListingService {
	
	private final FoodListingRepository foodListingRepository;
	
	private final UserRepository userRepository;

	@Override
	public FoodListing createFoodListing(CreateFoodListingRequest request) {
		
		userRepository.findById(request.getHostId())
				.orElseThrow(()-> new UserNotFoundException("User not found"));
		
//		System.out.println(user);
//		if(user.isEmpty()) {
//			throw new UserNotFoundException("User not found");
//		}
		
		
		FoodListing foodListing = FoodListing.builder()
				.city(request.getCity())
				.cost(request.getCost())
				.foodName(request.getFoodName())
				.foodType(request.getFoodType())
				.latitude(request.getLatitude())
				.longitude(request.getLongitude())
				.hostId(request.getHostId())
				.quantity(request.getQuantity())
				.status(FoodStatus.AVAILABLE)
				.expiryTime(request.getExpiryTime())
				.build();
		
		return foodListingRepository.save(foodListing);
	}

}
