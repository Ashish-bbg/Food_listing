package com.food.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.food.entity.FoodListing;

public interface FoodListingRepository extends JpaRepository<FoodListing, UUID> {
	

//	@Lock(LockModeType.PESSIMISTIC_WRITE)
//	Optional<FoodListing> findById(UUID id);
	
	@Modifying
	@Query("""
			UPDATE FoodListing f
			 SET f.status = CASE WHEN(f.quantity-:quantity=0) THEN 'RESERVED' ELSE 'AVAILABLE' END,
			 f.quantity = f.quantity - :quantity
			where f.id = :foodId 
			AND f.quantity >= :quantity
			AND f.status = 'AVAILABLE'
			""")
	int reserveFood(UUID foodId, Integer quantity);
	
}
