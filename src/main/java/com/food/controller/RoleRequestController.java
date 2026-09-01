package com.food.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.dto.request.CreateRoleRequest;
import com.food.dto.response.RoleRequestResponse;
import com.food.service.RoleRequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role-requests")
public class RoleRequestController {
	
	private final RoleRequestService roleRequestService;

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<RoleRequestResponse> createRoleRequest(
			@Valid @RequestBody CreateRoleRequest request){
		
		return ResponseEntity.ok(
				roleRequestService.createRoleRequest(request)
				);
	}
	
}
