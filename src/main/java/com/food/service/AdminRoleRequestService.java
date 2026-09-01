package com.food.service;

import java.util.List;

import com.food.dto.response.AdminRoleRequestResponse;

public interface AdminRoleRequestService {
	
	List<AdminRoleRequestResponse> getPendingRoleRequest();
		
}
