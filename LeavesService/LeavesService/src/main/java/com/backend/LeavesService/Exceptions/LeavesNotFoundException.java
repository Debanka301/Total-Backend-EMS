package com.backend.LeavesService.Exceptions;

public class LeavesNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMessage;
	
	public LeavesNotFoundException() {
		
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

	public LeavesNotFoundException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}
	
	

}
