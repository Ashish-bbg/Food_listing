package com.food.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.dto.request.FoodListingRequest;
import com.food.dto.response.FoodListingResponse;
import com.food.service.FoodListingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/food-listings")
public class FoodListingController {
	
	private final FoodListingService foodListingService;

	// Create food listing
	@PostMapping
	@PreAuthorize("hasRole('EVENT_HOST')")
	public ResponseEntity<String> createFoodListing(@Valid @RequestBody FoodListingRequest request){
		
		foodListingService.createFoodListing(request);
		
		return ResponseEntity.ok("Food listing created successfully");
	}
	
	// Get All food Listings
	@GetMapping
	public ResponseEntity<Page<FoodListingResponse>> getAllFoodListings(
			@PageableDefault(
					page=0,
					size=10,
					sort="createdAt",
					direction=Sort.Direction.DESC) 
			Pageable pageable){
		
		pageable = validatePageable(pageable);
	
		return ResponseEntity.ok(foodListingService.getAllFoodListing(pageable));			
	
	}
	
	// Get food by Id
	@GetMapping("/{id}")
	public ResponseEntity<FoodListingResponse> getFoodById(@PathVariable UUID id){
		return ResponseEntity.ok(foodListingService.getFoodById(id));
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteFoodById(@PathVariable UUID id){
		return ResponseEntity.ok(foodListingService.deleteFoodById(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<FoodListingResponse> updateFoodById(@PathVariable UUID id, @Valid @RequestBody FoodListingRequest request){
		return ResponseEntity.ok(foodListingService.updateFoodById(id, request));
	}
	
	private Pageable validatePageable(Pageable pageable) {
		if(pageable.getPageSize()>50)
			return PageRequest.of(pageable.getPageNumber(), 50, pageable.getSort());
		return pageable;
	}
	
}
