package com.backend.TaxService.Exceptions;

public class TaxDetailsNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMessage;
	public TaxDetailsNotFoundException() {
		
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

	public TaxDetailsNotFoundException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}
	
	
	
	

}
