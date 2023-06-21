package com.backend.EmployeeService.Controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.EmployeeService.DTO.AuthRequest;
import com.backend.EmployeeService.DTO.AuthResponseDTO;
import com.backend.EmployeeService.Entity.Employee;
import com.backend.EmployeeService.Entity.LoginDTO;
import com.backend.EmployeeService.Entity.Tax;
import com.backend.EmployeeService.Exceptions.EmployeeNotFoundException;
import com.backend.EmployeeService.Exceptions.InputErrorException;
import com.backend.EmployeeService.Exceptions.TokenInvalidException;
import com.backend.EmployeeService.Service.EmployeeService;
import com.backend.EmployeeService.Service.JwtService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
public class EmployeeController {
	
	Logger logger= LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	private EmployeeService empService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@GetMapping("/employee/admin/all")
	@PreAuthorize("hasAuthority('Admin')")
	public List<Employee> getAllEmployees(){
		logger.info("All Employees information");
		return empService.getAllEmployee();
	}
	
	@GetMapping("/employee/normal/{id}")
	@PreAuthorize("hasAnyAuthority('User','Admin')")
	public ResponseEntity<Optional<Employee>> getEmployeeById(@PathVariable Integer id) throws EmployeeNotFoundException{
		logger.info("Employee Details By Id");
		return empService.getEmployeeById(id);
	}
	
	@PostMapping("/employee/admin/save")
	@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<Employee> saveEmployee(@RequestBody  Employee employee) throws InputErrorException{
		logger.info("Saving Employee");
		return empService.saveEmployee(employee);
	}
	
	@PutMapping("/employee/normal/update/{id}")
	@PreAuthorize("hasAnyAuthority('User','Admin')")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee, @PathVariable Integer id) throws EmployeeNotFoundException{
		logger.info("Updating Employee");
		return empService.updateEmployee(employee, id);
	}
	
	@DeleteMapping("/employee/admin/delete/{id}")
	@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<String> deleteEmployee(@PathVariable Integer id) {
		logger.info("Deleting Employee");
		return empService.deleteEmployee(id);
	}

	@GetMapping("/employee/normal/leaves/{id}")
	@PreAuthorize("hasAnyAuthority('User','Admin')")
	public ResponseEntity<Employee> getEmployeeWithLeaves(@PathVariable Integer id, @RequestHeader(name=HttpHeaders.AUTHORIZATION) String token) throws EmployeeNotFoundException,TokenInvalidException{
		logger.info("Get Employee's Leaves");
		return empService.getAllLeavesByUserId(id, token);
	}
	
	@GetMapping("/employee/normal/tax/{id}")
	@PreAuthorize("hasAnyAuthority('User','Admin')")
		public ResponseEntity<Tax> getTaxByEmpId(@PathVariable Integer id, @RequestHeader(name=HttpHeaders.AUTHORIZATION) String token) throws JsonMappingException, JsonProcessingException, EmployeeNotFoundException,TokenInvalidException{
		logger.info("Get Employee's Tax Details");
		return empService.getTaxByEmpId(id, token);
	}
	
	@PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		logger.info("Employee Login");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
        	Optional<Employee> emp = empService.getEmployeeByName(authRequest.getName());
        	Employee employee= emp.get();
        	String token= jwtService.generateToken(authRequest.getName());
        	logger.info(token);
        	AuthResponseDTO responseDTO= new AuthResponseDTO();
        	responseDTO.setToken(token);
        	responseDTO.setEmployee(employee);
        	
        	HttpHeaders headers= new HttpHeaders();
        	//headers.add("Access-Control-Exposure-Headers", "Authorization");
        	headers.add("Authorization", "Bearer:"+token);
        	
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(responseDTO);
        } else {
        	logger.error("Employee Not Found");
            throw new UsernameNotFoundException("invalid user request !");
        }


    }
	
	@GetMapping("/validateToken")
	 public String isTokenValid(@RequestHeader(name=HttpHeaders.AUTHORIZATION) String token) {
		logger.info("Token is validated");
		 return this.jwtService.isTokenValid(token.substring(7));
	 }

}
