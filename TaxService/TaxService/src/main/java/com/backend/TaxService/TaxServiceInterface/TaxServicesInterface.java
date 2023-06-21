package com.backend.TaxService.TaxServiceInterface;

import org.springframework.http.ResponseEntity;

import com.backend.TaxService.Entity.Tax;
import com.backend.TaxService.Entity.TaxInput;

public interface TaxServicesInterface {
	
	public ResponseEntity<Tax> saveTax(TaxInput taxInput, String token);
	
	public int calculateTaxPerc(Integer salary);
	
	public int calculateTaxAmount(Integer salary, Integer taxPerc);
	
	public int calculateInHand(Integer salary, Integer taxAmount);
	
	public ResponseEntity<Tax> getTaxByEmpId(Integer empId, String token);

}
