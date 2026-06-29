package com.food.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

	@ExceptionHandler(exception = UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex){
		
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(exception = FoodNotFoundException.class)
	public ResponseEntity<String> handleFoodNotFound(FoodNotFoundException ex){
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(exception = NotEnoughFoodAvailableException.class)
	public ResponseEntity<String> handleNotEnoughFoodAvailable(NotEnoughFoodAvailableException ex){
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(exception = FoodUnavailableException.class)
	public ResponseEntity<String> handleFoodUnavailable(FoodUnavailableException ex){
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(exception = FoodClaimNotFoundException.class)
	public ResponseEntity<String> handleFoodClaimNotFound(FoodClaimNotFoundException ex){
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(exception = InvalidClaimStateException.class)
	public ResponseEntity<String> handleInvalidClaimState(InvalidClaimStateException ex){
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	
	@ExceptionHandler(exception = ObjectOptimisticLockingFailureException.class)
	public ResponseEntity<String> handleOptimisticsLock(ObjectOptimisticLockingFailureException ex){
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body("Someone else modified this food listing. Please try again.");
	}
	
	@ExceptionHandler(exception = InvalidCredentialsException.class)
	public ResponseEntity<String> handleInvalidCredential(InvalidCredentialsException ex){
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(ex.getMessage());
	}
	
	
}
