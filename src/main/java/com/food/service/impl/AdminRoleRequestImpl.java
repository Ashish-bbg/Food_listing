package com.food.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.food.dto.response.AdminRoleRequestResponse;
import com.food.entity.RoleRequest;
import com.food.enums.RoleRequestStatus;
import com.food.repository.RoleRequestRepository;
import com.food.service.AdminRoleRequestService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminRoleRequestImpl implements AdminRoleRequestService{
	
	private final RoleRequestRepository roleRequestRepository;

	@Override
	public List<AdminRoleRequestResponse> getPendingRoleRequest() {
		
		List<RoleRequest> roleRequests = roleRequestRepository.findByStatus(RoleRequestStatus.PENDING);
		
		if(roleRequests.isEmpty()) {
			return new ArrayList<>();
		}
		
		List<AdminRoleRequestResponse> adminRoleRequests = new ArrayList<AdminRoleRequestResponse>();
		
		
		for(RoleRequest role: roleRequests) {
			AdminRoleRequestResponse adminRole = AdminRoleRequestResponse.builder()
				.id(role.getId())
				.userId(role.getUserId())
				.userName(null)
				.email(null)
				.requestedRole(role.getRequestedRole())
				.status(role.getStatus())
				.createdAt(role.getCreatedAt())
				.build();
			
			adminRoleRequests.add(adminRole);
			
		}
		
		return adminRoleRequests;
	}

}
