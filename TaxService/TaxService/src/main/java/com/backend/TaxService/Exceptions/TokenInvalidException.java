package com.backend.TaxService.Exceptions;

public class TokenInvalidException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMessage;
	
	public TokenInvalidException() {
		
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public TokenInvalidException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	
}
