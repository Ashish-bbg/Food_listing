package com.food.service;

import com.food.dto.request.CreateRoleRequest;
import com.food.dto.response.RoleRequestResponse;

public interface RoleRequestService {
	
	RoleRequestResponse createRoleRequest(CreateRoleRequest createRoleRequest);

}
