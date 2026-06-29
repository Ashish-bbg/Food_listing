package com.food.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.food.enums.UserRole;
import com.food.enums.VerificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable = false)
	@NotBlank(message = "Name cannot be empty")
	private String name;
	
	@Column(nullable = false, unique=true)
	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Please enter valid email format")
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@NotBlank(message = "Phone number cannot be empty")
	@Column(nullable = false, unique = true)
	private String phone;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private UserRole role;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private VerificationStatus verificationStatus;
	
	@Column(nullable=false, updatable = false)
	private LocalDateTime createdAt;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}
}
