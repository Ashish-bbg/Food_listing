package com.food.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.dto.response.AdminRoleRequestResponse;
import com.food.dto.response.RejectRoleRequest;
import com.food.dto.response.RoleRequestResponse;
import com.food.service.AdminRoleRequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/event-host-requests")
@RequiredArgsConstructor
public class AdminRoleRequestController {
	
	private final AdminRoleRequestService adminRoleRequestService;
	
	@GetMapping("/pending")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<AdminRoleRequestResponse>> getPendingRoleRequest(){
		return ResponseEntity.ok(adminRoleRequestService.getPendingRoleRequest());
	}
	
	@PostMapping("/{requestId}/approve")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<RoleRequestResponse> approveRoleRequest(@PathVariable UUID requestId){
		
		return ResponseEntity.ok(adminRoleRequestService.approveRoleRequest(requestId));
		
	}
	
	@PostMapping("/{requestId}/reject")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<RoleRequestResponse> rejectRoleRequest(
			@PathVariable UUID requestId,
			@Valid @RequestBody RejectRoleRequest rejectRoleRequest
			){
		
		RoleRequestResponse roleRequestResponse = adminRoleRequestService.rejectRoleRequest(requestId, rejectRoleRequest);
		return ResponseEntity.ok(roleRequestResponse);
		
	}

}
