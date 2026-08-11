package com.food.service;

import com.food.dto.request.CreateUserRequest;
import com.food.dto.request.UserRequest;
import com.food.dto.response.UserResponse;

public interface UserService {
	
	UserResponse createUser(CreateUserRequest request);
	
	UserResponse getCurrUser();
	
	UserResponse updateCurrUser(UserRequest request);
}
