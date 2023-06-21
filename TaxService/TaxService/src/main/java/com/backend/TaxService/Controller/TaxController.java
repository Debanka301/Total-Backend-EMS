package com.backend.TaxService.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.backend.TaxService.Entity.Tax;
import com.backend.TaxService.Entity.TaxInput;
import com.backend.TaxService.Exceptions.TaxDetailsNotFoundException;
import com.backend.TaxService.Exceptions.TokenInvalidException;
import com.backend.TaxService.TaxService.TaxService;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
public class TaxController {
	
	@Autowired
	private TaxService taxService;
	
	Logger logger= LoggerFactory.getLogger(TaxController.class);
	
	@PostMapping("/tax/save")
	public ResponseEntity<Tax> saveTax(@RequestBody @Valid TaxInput taxInput, @RequestHeader(name=HttpHeaders.AUTHORIZATION) String token) throws TokenInvalidException{
		logger.info("Saving Tax Details");
		return taxService.saveTax(taxInput, token);
	}
	
	@GetMapping("/tax/find/{empId}")
	public ResponseEntity<Tax> getTaxByEmpId(@PathVariable Integer empId, @RequestHeader(name=HttpHeaders.AUTHORIZATION) String token) throws TokenInvalidException,TaxDetailsNotFoundException{
		logger.info("Returning Tax of employee");
		return taxService.getTaxByEmpId(empId, token);
	}
	

}
