package com.food.exception;

public class LocationArgumentException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public LocationArgumentException(String message) {
		super(message);
	}

}
