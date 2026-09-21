package com.food.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.entity.NgoVerification;

public interface NgoVerificationRepository extends JpaRepository<NgoVerification, UUID>{

	Optional<NgoVerification> findByUser_Id(UUID userId);
	
	boolean existsByUser_Id(UUID userId);
	
}
