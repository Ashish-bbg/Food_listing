package com.food.exception;

public class NotEnoughFoodAvailableException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public NotEnoughFoodAvailableException(String message) {
		super(message);
	}
	

}
