package com.food.exception;

public class FoodClaimNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public FoodClaimNotFoundException(String message) {
		super(message);
	}
	

}
