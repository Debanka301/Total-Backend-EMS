package com.backend.TaxService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.backend.TaxService.Entity.Tax;
import com.backend.TaxService.Entity.TaxInput;
import com.backend.TaxService.Repository.TaxRepository;
import com.backend.TaxService.TaxService.TaxService;

@SpringBootTest
class TaxServiceApplicationTests {
	
	@Mock
	TaxRepository taxRepo;
	
	@InjectMocks
	TaxService taxService;
	
	 @Mock
	    private RestTemplate restTemplate;
	 
	 @BeforeEach
	    public void setup() {
	        MockitoAnnotations.openMocks(this);
	        taxService = new TaxService(taxRepo, restTemplate);
	    }

	@Test
	void contextLoads() {
	}
	
	
	
	@Test
	void testCalculateTaxPerc() {
		int expected=20;
		assertEquals(expected, taxService.calculateTaxPerc(1200000));
	}
	
	@Test
	void testCalculateTaxAmount() {
		int expected=240000;
		assertEquals(expected, taxService.calculateTaxAmount(1200000, 20));
	}
	
	@Test
	void testCalculateInHand() {
		int expected=960000;
		assertEquals(expected, taxService.calculateInHand(1200000, 240000));
	}
	
	@Test
	void testSaveTax() {
		
		String token = "valid-token";
        TaxInput taxInput = new TaxInput(1, 800000);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> tokenResponse = ResponseEntity.ok("valid");
        
        when(restTemplate.exchange(
                eq("http://EMPLOYEE-SERVICE/validateToken"),
                eq(HttpMethod.GET),
                eq(requestEntity),
                eq(String.class)
        )).thenReturn(tokenResponse);
        
        ResponseEntity<Tax> response = taxService.saveTax(taxInput, token);
        
        verify(restTemplate, times(1)).exchange(
                eq("http://EMPLOYEE-SERVICE/validateToken"),
                eq(HttpMethod.GET),
                eq(requestEntity),
                eq(String.class)
        );
	
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getEmpId());
        assertEquals(800000, response.getBody().getSalary());
        assertEquals(720000, response.getBody().getInHand());
        assertEquals(80000, response.getBody().getTaxAmount());
        assertEquals(10, response.getBody().getTaxPerc());
		
	}
	
	@Test
	void testTaxByEmpId() {
		String token = "valid-token";
        Integer empId = 1;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> tokenResponse = ResponseEntity.ok("valid");
        
        Tax taxResult = new Tax(1,1,800000,10,80000,720000);
        
        when(restTemplate.exchange(
                eq("http://EMPLOYEE-SERVICE/validateToken"),
                eq(HttpMethod.GET),
                eq(requestEntity),
                eq(String.class)
        )).thenReturn(tokenResponse);

        when(taxRepo.getTaxByEmpId(empId)).thenReturn(taxResult);
        ResponseEntity<Tax> response = taxService.getTaxByEmpId(empId, token);
        
        verify(restTemplate, times(1)).exchange(
                eq("http://EMPLOYEE-SERVICE/validateToken"),
                eq(HttpMethod.GET),
                eq(requestEntity),
                eq(String.class)
        );

        verify(taxRepo, times(1)).getTaxByEmpId(empId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taxResult, response.getBody());
		
	}
	

}
