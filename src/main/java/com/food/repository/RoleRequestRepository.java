package com.food.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.food.entity.RoleRequest;
import com.food.enums.RoleRequestStatus;

public interface RoleRequestRepository extends JpaRepository<RoleRequest, UUID> {

	boolean existsByUserIdAndStatus(UUID id, RoleRequestStatus roleRequestStatus);
	
	List<RoleRequest> findByStatus(RoleRequestStatus status);
	
}
