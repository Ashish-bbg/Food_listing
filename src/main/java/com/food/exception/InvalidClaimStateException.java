package com.food.exception;

public class InvalidClaimStateException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public InvalidClaimStateException(String message) {
		super(message);
	}
}
