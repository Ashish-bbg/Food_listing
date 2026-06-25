package com.food.exception;

public class FoodUnavailableException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public FoodUnavailableException(String message) {
		super(message);
	}

}
