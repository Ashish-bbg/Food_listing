package com.food.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.dto.CreateFoodListingRequest;
import com.food.service.FoodListingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/food-listings")
public class FoodListingController {
	
	private final FoodListingService foodListingService;

	@PostMapping
	public ResponseEntity<String> createFoodListing(@Valid @RequestBody CreateFoodListingRequest request){
		
		foodListingService.createFoodListing(request);
		
		return ResponseEntity.ok("Food listing created successfully");
	}

}
