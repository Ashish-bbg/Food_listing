package com.food.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.food.entity.FoodListing;

import jakarta.persistence.LockModeType;

public interface FoodListingRepository extends JpaRepository<FoodListing, UUID> {
	

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<FoodListing> findById(UUID id);
	
}
