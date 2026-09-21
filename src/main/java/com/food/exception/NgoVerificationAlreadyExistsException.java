package com.food.exception;

public class NgoVerificationAlreadyExistsException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public NgoVerificationAlreadyExistsException(String message) {
		super(message);
	}

}
