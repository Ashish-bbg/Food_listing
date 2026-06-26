package com.food.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.entity.FoodClaim;
import com.food.enums.FoodClaimStatus;

public interface FoodClaimRepository extends JpaRepository<FoodClaim, UUID>{

	public List<FoodClaim> findByStatusAndReservedUntilBefore(FoodClaimStatus status, LocalDateTime time);
	
}
