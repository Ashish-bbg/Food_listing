package com.food.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.food.entity.RoleRequest;
import com.food.enums.RoleRequestStatus;

public interface RoleRequestRepository extends JpaRepository<RoleRequest, UUID> {

	boolean existsByUser_IdAndStatus(UUID id, RoleRequestStatus roleRequestStatus);
	
	@Query("""
	   SELECT r
	   FROM RoleRequest r
	   JOIN FETCH r.user
	   where r.status = :status			
	""")
	List<RoleRequest> findByStatusWithUser(RoleRequestStatus status);
	
	List<RoleRequest> findByUser_IdOrderByCreatedAtDesc(UUID userId);
	
}
