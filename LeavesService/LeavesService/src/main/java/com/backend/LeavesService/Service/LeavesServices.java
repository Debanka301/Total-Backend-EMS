package com.backend.LeavesService.Service;

import java.util.List;
import java.util.Optional;

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

import com.backend.LeavesService.Controller.LeavesController;
import com.backend.LeavesService.Entity.Leaves;
import com.backend.LeavesService.Exceptions.LeavesNotFoundException;
import com.backend.LeavesService.Exceptions.TokenInvalidException;
import com.backend.LeavesService.LeavesServiceInterface.LeavesServicesInterface;
import com.backend.LeavesService.Repository.LeavesRepository;

@Service
public class LeavesServices implements LeavesServicesInterface{
	
	Logger logger= LoggerFactory.getLogger(LeavesServices.class);
	
	@Autowired
	private LeavesRepository LeavesRepo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public List<Leaves> getLeaveByUserId(Integer userId, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", token);
		
		HttpEntity<Void> requestEntity= new HttpEntity<>(headers);
		ResponseEntity<String> res= restTemplate.exchange("http://EMPLOYEE-SERVICE:8091/validateToken", HttpMethod.GET, requestEntity, String.class);
		
		String respo= res.getBody().toString();
		logger.info(respo);
		if(respo.equals("valid")) {
			return LeavesRepo.getLeavesByEmployeeId(userId);
		}
		else {
			logger.error("Token invalid");
			throw new TokenInvalidException("Token is invalid!");
		}
	}
	
	public  ResponseEntity<Leaves> saveLeaves(Leaves leaves, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", token);
		
		HttpEntity<Void> requestEntity= new HttpEntity<>(headers);
		ResponseEntity<String> res= restTemplate.exchange("http://EMPLOYEE-SERVICE/validateToken", HttpMethod.GET, requestEntity, String.class);
		
		String respo= res.getBody().toString();
		logger.info(respo);
		
		if(respo.equals("valid")) {
			try {
			LeavesRepo.save(leaves);
			return ResponseEntity.status(HttpStatus.OK).body(leaves);
			}catch(Exception e) {
				throw new LeavesNotFoundException("Leaves of employee not found!!");
			}
		}else{
			logger.error("Token Invalid");
			throw new TokenInvalidException("Token is invalid!");

		}
	}
	
}
