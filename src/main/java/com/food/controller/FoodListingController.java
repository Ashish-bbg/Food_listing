package com.food.controller;

import java.util.Set;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.dto.request.FoodListingRequest;
import com.food.dto.response.FoodListingResponse;
import com.food.enums.FoodType;
import com.food.exception.LocationArgumentException;
import com.food.exception.SortArgumentException;
import com.food.service.FoodListingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/food-listings")
public class FoodListingController {
	
	private final FoodListingService foodListingService;
	
	private static final Set<String> ALLOWED_SORT_FILTER = Set.of("createdAt", "cost", "expiryTime", "foodName"); 

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
			@RequestParam(required = false) FoodType foodType,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) Double maxCost,			
			@RequestParam(required = false) Double minCost,			
			@RequestParam(required = false) Integer minQuantity,			
			@RequestParam(required = false) Double latitude,			
			@RequestParam(required = false) Double longitude,			
			@RequestParam(required = false) Double radiusKm,			
			@PageableDefault(
					page=0,
					size=10,
					sort="createdAt",
					direction=Sort.Direction.DESC) 
			Pageable pageable){
		
		validateLocation(latitude, longitude, radiusKm);
		pageable = validatePageable(pageable);
	
		return ResponseEntity.ok(
				foodListingService
				.getAllFoodListing(foodType, city,
						maxCost, minCost,
						minQuantity,
						latitude, longitude, radiusKm,
						pageable));			
	
	}
	
	// Get food by Id
	@GetMapping("/{id}")
	public ResponseEntity<FoodListingResponse> getFoodById(@PathVariable UUID id){
		return ResponseEntity.ok(foodListingService.getFoodById(id));
	}
	
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('EVENT_HOST')")
	public ResponseEntity<String> deleteFoodById(@PathVariable UUID id){
		return ResponseEntity.ok(foodListingService.deleteFoodById(id));
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('EVENT_HOST')")
	public ResponseEntity<FoodListingResponse> updateFoodById(@PathVariable UUID id, @Valid @RequestBody FoodListingRequest request){
		return ResponseEntity.ok(foodListingService.updateFoodById(id, request));
	}
	
	private Pageable validatePageable(Pageable pageable) {
		if(pageable.getPageSize()>50)
			pageable =  PageRequest.of(pageable.getPageNumber(), 50, pageable.getSort());
		
		for(Sort.Order order : pageable.getSort()) {
			if(!ALLOWED_SORT_FILTER.contains(order.getProperty())) {
				throw new SortArgumentException("Sorting by " + order.getProperty() + " is not allowed");
			}
		}
		return pageable;
	}
	
	private void validateLocation(Double latitude, Double longitude, Double radiusKm) {
		
		if(latitude != null && (latitude < -90 || latitude > 90)) {
			throw new LocationArgumentException("Latitude must be between -90 and 90");
		}
		
		if (longitude != null && (longitude < -180 || longitude > 180)) {
		        throw new LocationArgumentException("Longitude must be between -180 and 180");
		}
		
		if(radiusKm != null && radiusKm <=0) {
			 throw new LocationArgumentException("Radius must be greater than 0");
		}
		
		if((latitude != null || longitude !=null || radiusKm !=null ) &&
				(latitude == null || longitude == null || radiusKm == null)) {
			throw new LocationArgumentException("Latitude, longitude and radiusKm must be provided together");
		}
		 
		
	}
	
}
