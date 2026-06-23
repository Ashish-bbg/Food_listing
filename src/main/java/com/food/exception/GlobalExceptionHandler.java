package com.food.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(exception = EmailAlreadyExistsException.class)
	public ResponseEntity<String> handleEmailExists(EmailAlreadyExistsException ex) {
//		System.out.println(ex);
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(exception = PhoneAlreadyExistsException.class)
	public ResponseEntity<String> handlePhoneExists(PhoneAlreadyExistsException ex){
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(exception = MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>>handle(MethodArgumentNotValidException ex){
		
		Map<String, String> errors = new HashMap<String, String>();
		
		ex.getBindingResult()
		  .getFieldErrors()
		  .forEach(error-> errors.put(error.getField(), error.getDefaultMessage()));
		
		return ResponseEntity.badRequest().body(errors);
//		return ResponseEntity.badRequest().body("Validation Failed");
	}

}
