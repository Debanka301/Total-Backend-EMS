package com.backend.TaxService.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = TokenInvalidException.class)
	public ResponseEntity<?> handleTokenInvalidException(TokenInvalidException exception){
		return new ResponseEntity<String>(exception.getErrorMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = TaxDetailsNotFoundException.class)
	public ResponseEntity<?> handleTaxDetailsNotFoundException(TaxDetailsNotFoundException exception){
		return new ResponseEntity<String>(exception.getErrorMessage(), HttpStatus.BAD_REQUEST);
	}


}
