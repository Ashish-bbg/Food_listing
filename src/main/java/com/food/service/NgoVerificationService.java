package com.food.service;

import com.food.dto.request.CreateNgoVerification;
import com.food.dto.response.NgoVerificationResponse;

public interface NgoVerificationService {

	NgoVerificationResponse createVerification(CreateNgoVerification request);
	
}
