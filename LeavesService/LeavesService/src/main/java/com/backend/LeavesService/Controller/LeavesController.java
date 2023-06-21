package com.backend.LeavesService.Controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.backend.LeavesService.Entity.Leaves;
import com.backend.LeavesService.Exceptions.LeavesNotFoundException;
import com.backend.LeavesService.Exceptions.TokenInvalidException;
import com.backend.LeavesService.Service.LeavesServices;

@CrossOrigin("*")
@RestController
public class LeavesController {
	
	Logger logger= LoggerFactory.getLogger(LeavesController.class);
	
	@Autowired
	private LeavesServices leavesServices;
	
	@GetMapping("/leaves/{id}")
	public List<Leaves> getLeavesByUserId(@PathVariable Integer id, @RequestHeader(name=HttpHeaders.AUTHORIZATION) String token) throws LeavesNotFoundException,TokenInvalidException{
		logger.info("Returning Leaves By Empid");
		return leavesServices.getLeaveByUserId(id,token);
	}
	
	@PostMapping("/leaves/save")
	public ResponseEntity<Leaves> saveLeaves(@RequestBody Leaves leaves, @RequestHeader(name=HttpHeaders.AUTHORIZATION) String token) throws TokenInvalidException{
		logger.info("Saving leaves Information");
		return leavesServices.saveLeaves(leaves,token);
	}

}
