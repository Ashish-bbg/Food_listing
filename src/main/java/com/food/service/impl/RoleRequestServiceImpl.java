package com.food.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.food.dto.request.CreateRoleRequest;
import com.food.dto.response.RoleRequestResponse;
import com.food.entity.RoleRequest;
import com.food.entity.User;
import com.food.enums.RoleRequestStatus;
import com.food.enums.UserRole;
import com.food.exception.InvalidRoleRequestException;
import com.food.exception.RoleAlreadyAssignedException;
import com.food.exception.RoleRequestAlreadyPendingException;
import com.food.exception.UserNotFoundException;
import com.food.repository.RoleRequestRepository;
import com.food.repository.UserRepository;
import com.food.security.SecurityUtils;
import com.food.service.RoleRequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleRequestServiceImpl implements RoleRequestService{
	
	private final RoleRequestRepository roleRequestRepository;
	
	private final UserRepository userRepository;
	
	private static final Set<UserRole> ALLOWED_APPROVED_ROLES = Set.of(
			
			UserRole.EVENT_HOST,
			UserRole.NGO
			);

	
	@Override
	public RoleRequestResponse createRoleRequest(CreateRoleRequest createRoleRequest) {
				
		User user = userRepository.findById(SecurityUtils.getCurrentUserId())
				.orElseThrow(()-> new UserNotFoundException("User not found"));
		
		checkallowedRole(createRoleRequest);
		
		if(user.getRole() == createRoleRequest.getRequestedRole()) {
			throw new RoleAlreadyAssignedException("You are already a " + createRoleRequest.getRequestedRole());
		}
		
		if(roleRequestRepository.existsByUser_IdAndStatus(
				user.getId(),
				RoleRequestStatus.PENDING)) {
			throw new RoleRequestAlreadyPendingException("Your role request is already pending");
		}
		
		RoleRequest roleRequest = RoleRequest.builder()
				.user(user)
				.requestedRole(createRoleRequest.getRequestedRole())
				.status(RoleRequestStatus.PENDING)
				.createdAt(LocalDateTime.now())				
				.build();
		
		RoleRequest savedRequest = roleRequestRepository.save(roleRequest);
		
		return toRoleRequestResponse(savedRequest);
		
	}
	
	@Override
	public List<RoleRequestResponse> getMyRoleRequests() {
				
		UUID userId = SecurityUtils.getCurrentUserId();
		
		List<RoleRequest> roleRequests = roleRequestRepository
				.findByUser_IdOrderByCreatedAtDesc(userId);
		
		List<RoleRequestResponse> roleResponseList = new ArrayList<RoleRequestResponse>();
		
		for(RoleRequest savedRoleRequest: roleRequests) {
			roleResponseList.add(toRoleRequestResponse(savedRoleRequest));			
		}
				
		return roleResponseList;
	}

	
	private void checkallowedRole(CreateRoleRequest createRoleRequest) {
		
		if(ALLOWED_APPROVED_ROLES.contains(createRoleRequest.getRequestedRole())) {
			return;
			
		}
		
		throw new InvalidRoleRequestException("Only NGO and EVENT_HOST roles can be requested");
			
	}
	
	private RoleRequestResponse toRoleRequestResponse(RoleRequest roleRequest) {
		
		return RoleRequestResponse.builder()
		.id(roleRequest.getId())
		.requestedRole(roleRequest.getRequestedRole())
		.status(roleRequest.getStatus())
		.createdAt(roleRequest.getCreatedAt())
		.rejectionReason(roleRequest.getRejectionReason())
		.build();
		
	}
	
}
