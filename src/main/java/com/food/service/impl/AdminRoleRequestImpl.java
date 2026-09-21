package com.food.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.food.dto.response.AdminRoleRequestResponse;
import com.food.dto.response.RejectRoleRequest;
import com.food.dto.response.RoleRequestResponse;
import com.food.entity.RoleRequest;
import com.food.entity.User;
import com.food.enums.RoleRequestStatus;
import com.food.enums.UserRole;
import com.food.exception.InvalidRoleRequestException;
import com.food.exception.RoleRequestNotFoundException;
import com.food.exception.RoleRequestNotPendingException;
import com.food.repository.RoleRequestRepository;
import com.food.security.CustomUserDetails;
import com.food.service.AdminRoleRequestService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminRoleRequestImpl implements AdminRoleRequestService{
	
	private final RoleRequestRepository roleRequestRepository;

	@Override
	public List<AdminRoleRequestResponse> getPendingRoleRequest() {
		
		List<RoleRequest> roleRequests = roleRequestRepository.findByStatusWithUser(RoleRequestStatus.PENDING);
		
		if(roleRequests.isEmpty()) {
			return new ArrayList<>();
		}
		
		List<AdminRoleRequestResponse> adminRoleRequests = new ArrayList<AdminRoleRequestResponse>();
		
		
		for(RoleRequest role: roleRequests) {
			AdminRoleRequestResponse adminRole =
					AdminRoleRequestResponse.builder()
						.id(role.getId())
						.userId(role.getUser().getId())
						.userName(role.getUser().getName())
						.email(role.getUser().getEmail())
						.requestedRole(role.getRequestedRole())
						.status(role.getStatus())
						.createdAt(role.getCreatedAt())
						.build();
			
			adminRoleRequests.add(adminRole);
			
		}
		
		return adminRoleRequests;
	}

	@Override
	@Transactional
	public RoleRequestResponse approveRoleRequest(UUID requestId) {
		
		RoleRequest roleRequest = roleRequestRepository.findById(requestId)
			.orElseThrow(()->
				new RoleRequestNotFoundException("Role request not found"));
		
		if(roleRequest.getStatus() != RoleRequestStatus.PENDING) {
			throw new RoleRequestNotPendingException("Role request has already been processed");
		}
		
		Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		
		CustomUserDetails customeCustomUserDetails = (CustomUserDetails) authentication.getPrincipal();
		
		UUID adminId = customeCustomUserDetails.getUser().getId();
		
		User user = roleRequest.getUser();
		
		UserRole role = roleRequest.getRequestedRole();
		
		if(
				role != UserRole.EVENT_HOST ||
				role != UserRole.NGO) {
			throw new InvalidRoleRequestException("Invalid Role Request only EVENT and NGO are allowed");
		}
			
		user.setRole(role);
	
		roleRequest.setStatus(RoleRequestStatus.APPROVED);
		roleRequest.setReviewedBy(adminId);
		roleRequest.setReviewedAt(LocalDateTime.now());
		
		roleRequestRepository.save(roleRequest);
		
		RoleRequestResponse.builder()
		.id(roleRequest.getId())
		.requestedRole(roleRequest.getRequestedRole())
		.status(roleRequest.getStatus())
		.createdAt(roleRequest.getCreatedAt())
		.build();
		
		return null;
	}

	@Override
	@Transactional
	public RoleRequestResponse rejectRoleRequest(UUID requestId, RejectRoleRequest rejectRoleRequest) {
		
		RoleRequest roleRequest = roleRequestRepository.findById(requestId)
			.orElseThrow(()->
					new RoleRequestNotFoundException("Role request not found"));
		
		if(roleRequest.getStatus() != RoleRequestStatus.PENDING) {
			throw new RoleRequestNotPendingException("Role request has already been processed");
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		CustomUserDetails customUserDetails  = (CustomUserDetails) authentication.getPrincipal();
		
		UUID adminId = customUserDetails.getUser().getId();
		
		roleRequest.setStatus(RoleRequestStatus.REJECTED);
		roleRequest.setRejectionReason(rejectRoleRequest.getRejectionReason());
		roleRequest.setReviewedBy(adminId);
		roleRequest.setReviewedAt(LocalDateTime.now());
		
		return RoleRequestResponse.builder()
			.id(roleRequest.getId())
			.requestedRole(roleRequest.getRequestedRole())
			.status(roleRequest.getStatus())
			.createdAt(roleRequest.getCreatedAt())
			.build();
		
	}

}
