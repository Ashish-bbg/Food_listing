package com.food.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.dto.request.CreateNgoVerification;
import com.food.dto.response.NgoVerificationResponse;
import com.food.service.NgoVerificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/*********************

KEEPIGN THIS FILE ON HOLD FOR NOW WILL WORK ON VERIFICATION FOR NGO AND EVENT
NEED TO CHECK ON THERE FILE IF WORKING ON VERIFICATION 
1. NgoVerification (entity)
2. NgoVerification  (controller)
3. CreateNgoVerification (Request)
4. NgoVerificationResponse (Dto Response)
5. NgoVerificationStatus (Enum)
6. NgoVerificationAlreadyExistsException (Exception)
7. Ngo VerficationRepository
8. RoleRequestSerivceImple
9. NgoVerificationServiceImpl

**********************/

@RestController
@RequestMapping("/ngo-verifications")
@RequiredArgsConstructor
public class NgoVerificationController {

	private final NgoVerificationService ngoVerificationService;
	
	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<NgoVerificationResponse> createVerification(
			@Valid @RequestBody CreateNgoVerification request){
		
		NgoVerificationResponse response = ngoVerificationService.createVerification(request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
}
