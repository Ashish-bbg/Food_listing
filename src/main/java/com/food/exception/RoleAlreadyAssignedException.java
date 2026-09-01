package com.food.exception;

public class RoleAlreadyAssignedException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public RoleAlreadyAssignedException(String message) {
		super(message);
	}
	
}
