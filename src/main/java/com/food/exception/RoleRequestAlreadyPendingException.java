package com.food.exception;


public class RoleRequestAlreadyPendingException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public RoleRequestAlreadyPendingException(String message) {
		super(message);
	}

}
