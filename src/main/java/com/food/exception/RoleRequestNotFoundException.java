package com.food.exception;

public class RoleRequestNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public RoleRequestNotFoundException(String message) {
		super(message);
	}

}
