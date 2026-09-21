package com.food.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.food.enums.NgoVerificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class NgoVerification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id; 
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;
	
	@Column(nullable = false)
	private String ngoName;
	
	@Column(nullable = false, length = 1000)
	private String description;
	
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false)
	private String city;

	private String website;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NgoVerificationStatus status = NgoVerificationStatus.PENDING; 
	
	private String rejectionReason;
	
	private UUID reviewedBy;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime reviewedAt;
	
}
