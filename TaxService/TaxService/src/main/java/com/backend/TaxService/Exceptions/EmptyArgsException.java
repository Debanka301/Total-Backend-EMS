package com.backend.TaxService.Exceptions;

public class EmptyArgsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMesage;
	
	public EmptyArgsException() {
		
	}

	public String getErrorMesage() {
		return errorMesage;
	}

	public void setErrorMesage(String errorMesage) {
		this.errorMesage = errorMesage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public EmptyArgsException(String errorMesage) {
		super();
		this.errorMesage = errorMesage;
	}
	

}
