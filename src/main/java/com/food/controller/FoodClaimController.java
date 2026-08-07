package com.food.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.dto.FoodClaimRequest;
import com.food.dto.response.FoodClaimResponse;
import com.food.service.FoodClaimService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/food-claims")
public class FoodClaimController {
	
	private final FoodClaimService foodClaimService;
	
	@PostMapping
	public ResponseEntity<FoodClaimResponse> claimFood(@Valid @RequestBody FoodClaimRequest request){		
		return ResponseEntity.ok(foodClaimService.claimFood(request));
	}
	
	@PostMapping("/{id}/confirm")
	public ResponseEntity<String> confirmFoodClaim(@PathVariable UUID id){
		foodClaimService.confirmFoodClaim(id);
		return ResponseEntity.ok("Food claim confirmed successfully");
	}

	@GetMapping("/my")
	public ResponseEntity<List<FoodClaimResponse>> getFoodByUserId(){
		return ResponseEntity.ok(foodClaimService.getMyClaims());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<FoodClaimResponse> getFoodClaimById(@PathVariable UUID id){
		return ResponseEntity.ok(foodClaimService.getClaimById(id));
	}
	
	@PutMapping("/{id}/cancel")
	public ResponseEntity<FoodClaimResponse> cancelFoodClaimById(@PathVariable UUID id){
		return ResponseEntity.ok(foodClaimService.cancelFoodClaimById(id));
	}
	
	
	
}
