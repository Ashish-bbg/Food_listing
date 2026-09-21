package com.food.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.food.enums.NgoVerificationStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NgoVerificationResponse {

	private UUID id;
	
	private String ngoName;
	
	private String description;
	
	private String address;
	
	private String city;
	
	private String website;
	
	private NgoVerificationStatus status;
	
	private String rejectionReason;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime reviewedAt;
	
}
