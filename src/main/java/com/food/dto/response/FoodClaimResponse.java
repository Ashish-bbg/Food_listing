package com.food.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.food.enums.FoodClaimStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodClaimResponse {

	private UUID id;
	
	private UUID foodId;
	
//	private UUID userId;
	
	private Integer quantity;
	
	@Enumerated(EnumType.STRING)
	private FoodClaimStatus status;
	
	private LocalDateTime reservedAt;
	
	private LocalDateTime reservedUntil;
	
	private LocalDateTime claimedAt;
	
	private LocalDateTime cancelledAt;
	
}
