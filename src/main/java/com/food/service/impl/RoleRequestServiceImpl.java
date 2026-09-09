package com.food.service.impl;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.food.security.CustomUserDetails;
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
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		
		User user = userRepository.findById(customUserDetails.getUser().getId())
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
		
		return RoleRequestResponse.builder()
		.id(savedRequest.getId())
		.requestedRole(savedRequest.getRequestedRole())
		.status(savedRequest.getStatus())
		.createdAt(savedRequest.getCreatedAt())
		.build();
		
	}
	
	private void checkallowedRole(CreateRoleRequest createRoleRequest) {
		
		if(ALLOWED_APPROVED_ROLES.contains(createRoleRequest.getRequestedRole())) {
			return;
			
		}
		
		throw new InvalidRoleRequestException("Only NGO and EVENT_HOST roles can be requested");
			
	}

}
