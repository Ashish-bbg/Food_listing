package com.food.exception;

public class FoodListingNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public FoodListingNotFoundException(String message) {
		super(message);
	}

}
