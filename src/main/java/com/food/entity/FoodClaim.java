package com.food.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.food.enums.FoodClaimStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "food_claims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodClaim {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private UUID foodId;
	
	@Column(nullable = false)
	private UUID userId;
	
	@Column(nullable = false)
	private Integer quantity;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private FoodClaimStatus status;
	
	@Column(nullable = false)
	private LocalDateTime reservedAt;
	
	@Column(nullable = false)
	private LocalDateTime reservedUntil;
	
	private LocalDateTime claimedAt;
	
	private LocalDateTime cancelledAt;
	
	@PrePersist
	public void prePersist() {
		this.reservedAt = LocalDateTime.now();
		this.status = FoodClaimStatus.RESERVED;
		this.reservedUntil = reservedAt.plusHours(2);
	}
	
}
