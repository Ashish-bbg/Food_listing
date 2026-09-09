package com.food.service;

import java.util.List;
import java.util.UUID;

import com.food.dto.response.AdminRoleRequestResponse;
import com.food.dto.response.RejectRoleRequest;
import com.food.dto.response.RoleRequestResponse;

public interface AdminRoleRequestService {
	
	List<AdminRoleRequestResponse> getPendingRoleRequest();
	
	RoleRequestResponse approveRoleRequest(UUID requestId);
	
	RoleRequestResponse rejectRoleRequest(UUID requestId, RejectRoleRequest rejectRoleRequest);
		
}
