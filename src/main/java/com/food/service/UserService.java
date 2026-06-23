package com.food.service;

import com.food.dto.CreateUserRequest;
import com.food.entity.User;

public interface UserService {
	
	User createUser(CreateUserRequest request);
	
}
