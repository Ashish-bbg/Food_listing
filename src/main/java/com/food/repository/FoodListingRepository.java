package com.food.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.food.entity.FoodListing;
import com.food.enums.FoodStatus;
import com.food.enums.FoodType;

public interface FoodListingRepository extends JpaRepository<FoodListing, UUID>, JpaSpecificationExecutor<FoodListing> {
	

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
	
	Page<FoodListing> findByStatusNot(FoodStatus status, Pageable pageable);
	
	
	Page<FoodListing> findByStatusNotAndFoodType(FoodStatus status, FoodType foodType, Pageable pageable);
	
	Page<FoodListing> findAll(Specification<FoodListing> spec, Pageable pageable);
	
}
