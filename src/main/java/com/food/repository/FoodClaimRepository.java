package com.food.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.entity.FoodClaim;

public interface FoodClaimRepository extends JpaRepository<FoodClaim, UUID>{

}
