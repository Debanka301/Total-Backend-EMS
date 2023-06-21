package com.backend.TaxService.TaxService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backend.TaxService.Controller.TaxController;
import com.backend.TaxService.Entity.Tax;
import com.backend.TaxService.Entity.TaxInput;
import com.backend.TaxService.Exceptions.EmptyArgsException;
import com.backend.TaxService.Exceptions.TaxDetailsNotFoundException;
import com.backend.TaxService.Exceptions.TokenInvalidException;
import com.backend.TaxService.Repository.TaxRepository;
import com.backend.TaxService.TaxServiceInterface.TaxServicesInterface;

@Service
public class TaxService implements TaxServicesInterface{
	
	Logger logger= LoggerFactory.getLogger(TaxService.class);
	
	@Autowired
	private TaxRepository taxRepo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public TaxService(TaxRepository taxRepo, RestTemplate restTemplate) {
		super();
		this.taxRepo = taxRepo;
		this.restTemplate = restTemplate;
	}


	
	public ResponseEntity<Tax> saveTax(TaxInput taxInput, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", token);
		
		HttpEntity<Void> requestEntity= new HttpEntity<>(headers);
		ResponseEntity<String> res= restTemplate.exchange("http://EMPLOYEE-SERVICE/validateToken", HttpMethod.GET, requestEntity, String.class);
		
		String respo= res.getBody().toString();
		logger.info(respo);
		
		if(respo.equals("valid")) {
			Tax tax= new Tax();
			tax.setEmpId(taxInput.getEmpId());
			tax.setSalary(taxInput.getSalary());
			tax.setTaxPerc(calculateTaxPerc(tax.getSalary()));
			tax.setTaxAmount(calculateTaxAmount(tax.getSalary(), tax.getTaxPerc()));
			tax.setInHand(calculateInHand(tax.getSalary(), tax.getTaxAmount()));
			
			taxRepo.save(tax);
			return ResponseEntity.status(HttpStatus.OK).body(tax);
		}else {
			logger.error("Token invalid");
			throw new TokenInvalidException("Token is invalid!!");
		}
		
	}
	
	public int calculateTaxPerc(Integer salary) {
		if(salary==0) {
			logger.error("Salary is empty");
			throw new EmptyArgsException("Salary Argument is empty!");
		}
		else if(salary>=1500000) {
			return 30;
		}
		else if(salary>=1000000 && salary<1500000) {
			return 20;
		}
		else if(salary>=700000 && salary<1000000) {
			return 10;
		}
		else {
			return 5;
		}
	}
	
	public int calculateTaxAmount(Integer salary, Integer taxPerc) {
		if(salary==0) {
			logger.error("Salary is empty");
			throw new EmptyArgsException("Tax Amount cannot be calculated as Salary is empty");
		}
		else if(taxPerc==0) {
			logger.error("taxPerc is empty");
			throw new EmptyArgsException("Tax Amount cannot be calculated as TaxPerc is empty");
		}
		return (salary*taxPerc)/100;
	}
	
	public int calculateInHand(Integer salary, Integer taxAmount) {
		if(salary==0) {
			logger.error("Salary is empty");
			throw new EmptyArgsException("Tax Amount cannot be calculated as Salary is empty");
		}
		else if(taxAmount==0) {
			logger.error("taxAmount is empty");
			throw new EmptyArgsException("Tax Amount cannot be calculated as TaxAmount is empty");
		}
		return salary-taxAmount;
	}
	
	public ResponseEntity<Tax> getTaxByEmpId(Integer empId, String token) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", token);
		
		HttpEntity<Void> requestEntity= new HttpEntity<>(headers);
		ResponseEntity<String> res= restTemplate.exchange("http://EMPLOYEE-SERVICE/validateToken", HttpMethod.GET, requestEntity, String.class);
		
		String respo= res.getBody().toString();
		logger.info(respo);
		
		if(respo.equals("valid")) {
			try {
			Tax result= taxRepo.getTaxByEmpId(empId);
			return ResponseEntity.status(HttpStatus.OK).body(result);
			}catch(Exception e){
				throw new TaxDetailsNotFoundException("Tax details are not found!!");
			}
		}
		else {
			logger.error("Token invalid");
			throw new TokenInvalidException("Token is invalid!!");
		}
	}

}
