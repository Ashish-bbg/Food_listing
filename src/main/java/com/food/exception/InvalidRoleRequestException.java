package com.food.exception;

public class InvalidRoleRequestException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public InvalidRoleRequestException(String message) {
		super(message);
	}

}
