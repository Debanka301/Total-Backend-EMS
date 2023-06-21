package com.backend.EmployeeService.Service;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.backend.EmployeeService.Controller.EmployeeController;
import com.backend.EmployeeService.EmployeeServiceInterfaces.EmployeeServiceInterface;
import com.backend.EmployeeService.Entity.Employee;
import com.backend.EmployeeService.Entity.Leaves;
import com.backend.EmployeeService.Entity.LoginDTO;
import com.backend.EmployeeService.Entity.Tax;
import com.backend.EmployeeService.Exceptions.EmployeeNotFoundException;
import com.backend.EmployeeService.Exceptions.InputErrorException;
import com.backend.EmployeeService.Exceptions.TokenInvalidException;
import com.backend.EmployeeService.Repository.EmployeeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmployeeService implements EmployeeServiceInterface{
	
	Logger logger= LoggerFactory.getLogger(EmployeeService.class);
	
	@Autowired
	private EmployeeRepository empRepo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PasswordEncoder encoder;
	
	public List<Employee> getAllEmployee(){
		return empRepo.findAll();
	}
	
	public ResponseEntity<Optional<Employee>> getEmployeeById(Integer id) {
		Optional<Employee> temp= empRepo.findById(id);
		if(temp.isEmpty()) {
			logger.error("Employee not found");
			throw new EmployeeNotFoundException("Employee not found");
		}
		logger.info("Employee Found");
		return ResponseEntity.status(HttpStatus.OK).body(temp);
	}
	
	public Optional<Employee> getEmployeeByName(String name){
		Optional<Employee> temp = Optional.of(empRepo.getEmployeeByName(name));
		if(temp.isEmpty()) {
			logger.error("Employee not found");
			throw new EmployeeNotFoundException("Employee not found");
		}
		logger.info("Employee Found");
		return temp;
	}
	
	public ResponseEntity<Employee> saveEmployee(Employee employee) {
		if(employee.getName()==null) {
			logger.error("Name is null");
			throw new InputErrorException("Name field cannot be null");
		}
		employee.setPassword(encoder.encode(employee.getPassword()));
		empRepo.save(employee);
		return ResponseEntity.status(HttpStatus.OK).body(employee);
	}
	
	public  ResponseEntity<Employee> updateEmployee(Employee employee, Integer id) {
		Optional<Employee> e= empRepo.findById(id);
		if(e.isEmpty()) {
			logger.error("Employee not found");
			throw new EmployeeNotFoundException("Employee not found");
		}
		Employee emp= e.get();
		emp.setAddress(employee.getAddress());
		emp.setAge(employee.getAge());
		emp.setName(employee.getName());
		empRepo.save(emp);
		return ResponseEntity.status(HttpStatus.OK).body(emp);
	}
	
	public ResponseEntity<String> deleteEmployee(Integer id) {
		empRepo.deleteById(id);
		String result= "Employee of id "+id+" is deleted";
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	public ResponseEntity<Employee> getAllLeavesByUserId(Integer id, String token){
		
		Optional<Employee> e= empRepo.findById(id);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", token);
		HttpEntity<Void> requestEntity= new HttpEntity<>(headers);
		
		if(e.isEmpty()) {
			throw new EmployeeNotFoundException("Employee not found");
		}
		Employee emp= e.get();
		ResponseEntity<List> leaves= restTemplate.exchange("http://LEAVES-SERVICE:8092/leaves/"+emp.getId(),HttpMethod.GET, requestEntity,List.class);
		List<Leaves> leavesByUser= leaves.getBody();
		logger.info("Leaves checking");
		emp.setAllLeaves(leavesByUser);
		try {
			logger.info("Leaves Found");
		return ResponseEntity.status(HttpStatus.OK).body(emp);
		}catch(Exception exp) {
			logger.error("Token invalid");
			throw new TokenInvalidException("Token is invalid!");
		}
	}
	
	public ResponseEntity<Tax> getTaxByEmpId(Integer id, String token) throws JsonMappingException, JsonProcessingException {
		
		ObjectMapper mapper= new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", token);
		
		HttpEntity<Void> requestEntity= new HttpEntity<>(headers);
		
		Optional<Employee> e = empRepo.findById(id);
		if(e.isEmpty()) {
			throw new EmployeeNotFoundException("Employee not found");
		}
		Employee emp= e.get();
		ResponseEntity<String> tax= restTemplate.exchange("http://TAX-SERVICE:8093/tax/find/"+emp.getId(), HttpMethod.GET, requestEntity, String.class);
		String respo= tax.getBody().toString();
		try {
			logger.info("Tax Found");
		Tax t= mapper.readValue(respo, Tax.class);
		return ResponseEntity.status(HttpStatus.OK).body(t);
		}catch(Exception exp) {
			logger.error("Token invalid");
			throw new TokenInvalidException("Token is invalid");
	}
		
	}
}
