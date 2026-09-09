package com.food.exception;

public class RoleRequestNotPendingException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public RoleRequestNotPendingException(String message) {
		super(message);
	}

}
