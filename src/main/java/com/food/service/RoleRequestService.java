package com.food.service;

import java.util.List;

import com.food.dto.request.CreateRoleRequest;
import com.food.dto.response.RoleRequestResponse;

public interface RoleRequestService {
	
	RoleRequestResponse createRoleRequest(CreateRoleRequest createRoleRequest);
	
	List<RoleRequestResponse> getMyRoleRequests();

}
