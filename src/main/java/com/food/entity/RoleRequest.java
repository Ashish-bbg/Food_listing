package com.food.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.food.enums.RoleRequestStatus;
import com.food.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private UUID userId;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private UserRole requestedRole;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RoleRequestStatus status = RoleRequestStatus.PENDING;
	
	private String rejectionReason;
	
	private UUID reviewedBy;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime reviewedAt;
	
}
