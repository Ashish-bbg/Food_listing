package com.food.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.food.enums.FoodStatus;
import com.food.enums.FoodType;

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
@Table(name = "food_listings")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodListing {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	

	@Column(nullable = false)
	private String foodName;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FoodType foodType;
	
	@Column(nullable=false)
	private Integer quantity;
	
	@Column(nullable=false)
	private Double cost;
	
	@Column(nullable=false)
	private String city;
	
	private Double latitude;
	
	private Double longitude;
	
	@Column(nullable=false)
	private LocalDateTime expiryTime;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private FoodStatus status;
	
	@Column(nullable=false)
	private UUID hostId;
	
	private LocalDateTime createdAt;
	
	@PrePersist
	public void prePersist() {
		 this.createdAt = LocalDateTime.now();
		 
		 if(status == null) {
			status = FoodStatus.AVAILABLE; 
		 }
	}
	
}
