package com.food.specification;


import org.springframework.data.jpa.domain.Specification;

import com.food.entity.FoodListing;
import com.food.enums.FoodStatus;
import com.food.enums.FoodType;

import jakarta.persistence.criteria.Expression;

public class FoodListingSpecification {

	public static Specification<FoodListing> isNotExpired() {
		return (root, query, criteriaBuilder) 
				-> criteriaBuilder
				.notEqual(root.get("status"),FoodStatus.EXPIRED);
	}
	
	public static Specification<FoodListing> hasFoodType(FoodType foodType) {
		return (root, query, criteriaBuilder)
				-> criteriaBuilder 
				.equal(root.get("foodType"), foodType);
	}
	
	public static Specification<FoodListing> hasCity(String city){
		return (root, query, criteriaBuilder) 
				-> criteriaBuilder
				.equal(root.get("city"), city);
	}
	
	public static Specification<FoodListing> hasMaxCost(Double maxCost){
		return (root, query, criteriaBuilder)
				-> criteriaBuilder.lessThanOrEqualTo(root.get("cost"), maxCost);
	}

	public static Specification<FoodListing> hasMinCost(Double minCost){
		return (root, query, criteriaBuilder)
				-> criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), minCost);
	}
	
	public static Specification<FoodListing> hasMinQuantity(Integer minQuantity){
		return (root, query, criteriaBuilder)
				-> criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), minQuantity);
	}
	
	public static Specification<FoodListing> withinDistance(Double latitude, Double longitude, Double radiusKm){
		
		return (root, query, criteriaBuilder) -> {
					
			Expression<Double> distance =
							criteriaBuilder.function(
									"ST_Distance_Sphere",
									Double.class,
							
							criteriaBuilder.function(
									"POINT",
									Object.class,
									root.get("longitude"),
									root.get("latitude")
									),
							
							criteriaBuilder.function(
									"POINT",
									Object.class,
									criteriaBuilder.literal(longitude),
									criteriaBuilder.literal(latitude)
									)
							
						);

			return criteriaBuilder.lessThanOrEqualTo(
					distance
					, radiusKm * 1000);
			
		};
	}
	
	
}
