package com.food.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.dto.FoodClaimRequest;
import com.food.service.FoodClaimService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/food-claims")
public class FoodClaimController {
	
	private final FoodClaimService foodClaimService;
	
	@PostMapping
	public ResponseEntity<String> claimFood(@Valid @RequestBody FoodClaimRequest request){
		
		foodClaimService.claimFood(request);
		
		return ResponseEntity.ok("Food have been claimed successfully");
	}
	
	@PostMapping("/{id}/confirm")
	public ResponseEntity<String> confirmFoodClaim(@PathVariable UUID id){
		foodClaimService.confirmFoodClaim(id);
		return ResponseEntity.ok("Food claim confirmed successfully");
	}

}
