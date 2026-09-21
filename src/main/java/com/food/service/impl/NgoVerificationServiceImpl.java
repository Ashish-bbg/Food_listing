package com.food.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.food.dto.request.CreateNgoVerification;
import com.food.dto.response.NgoVerificationResponse;
import com.food.entity.NgoVerification;
import com.food.entity.User;
import com.food.enums.NgoVerificationStatus;
import com.food.enums.RoleRequestStatus;
import com.food.enums.UserRole;
import com.food.exception.InvalidRoleRequestException;
import com.food.exception.NgoVerificationAlreadyExistsException;
import com.food.repository.NgoVerificationRepository;
import com.food.repository.RoleRequestRepository;
import com.food.security.SecurityUtils;
import com.food.service.NgoVerificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NgoVerificationServiceImpl implements NgoVerificationService {
	
	private final NgoVerificationRepository ngoVerificationRepository;
	
	private final RoleRequestRepository roleRequestRepository;

	@Override
	public NgoVerificationResponse createVerification(CreateNgoVerification request) {
		
		User user = SecurityUtils.getCurrentUser();
		
		boolean hasPendingNgoRequest = roleRequestRepository
				.existsByUser_IdAndRequestedRoleAndStatus(
						user.getId(), 
						UserRole.NGO,
						RoleRequestStatus.PENDING);
		
		if(!hasPendingNgoRequest) {
			throw new InvalidRoleRequestException("You do not have a pending NGO role request");
		}
		
		if(ngoVerificationRepository.existsByUser_Id(user.getId())) {
			throw new NgoVerificationAlreadyExistsException("NGO verification already submitted");
		}
		
		NgoVerification verification = NgoVerification.builder()
				.user(user)
				.ngoName(request.getNgoName())
				.description(request.getDescription())
				.address(request.getAddress())
				.city(request.getCity())
				.website(request.getWebsite())
				.status(NgoVerificationStatus.PENDING)
				.createdAt(LocalDateTime.now())
				.build();
		
		NgoVerification saved = ngoVerificationRepository.save(verification);
		
		return toResponse(saved);
	}
	
	private NgoVerificationResponse toResponse(NgoVerification verification) {
		
		return NgoVerificationResponse.builder()
				.id(verification.getId())
				.ngoName(verification.getNgoName())
				.description(verification.getDescription())
				.address(verification.getAddress())
                .city(verification.getCity())
                .website(verification.getWebsite())
                .status(verification.getStatus())
                .rejectionReason(verification.getRejectionReason())
                .createdAt(verification.getCreatedAt())
                .reviewedAt(verification.getReviewedAt())
				.build();
	}
	
	

}
