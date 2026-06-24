package com.food.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.entity.FoodListing;

public interface FoodListingRepository extends JpaRepository<FoodListing, UUID> {
	
	

}
