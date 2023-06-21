package com.backend.EmployeeService.EmployeeServiceInterfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.backend.EmployeeService.Entity.Employee;
import com.backend.EmployeeService.Entity.Tax;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface EmployeeServiceInterface {
	
	public List<Employee> getAllEmployee();
	
	public ResponseEntity<Optional<Employee>> getEmployeeById(Integer id);
	
	public Optional<Employee> getEmployeeByName(String name);
	
	public ResponseEntity<Employee> saveEmployee(Employee employee);
	
	public  ResponseEntity<Employee> updateEmployee(Employee employee, Integer id);
	
	public ResponseEntity<String> deleteEmployee(Integer id);
	
	public ResponseEntity<Employee> getAllLeavesByUserId(Integer id, String token);
	
	public ResponseEntity<Tax> getTaxByEmpId(Integer id, String token) throws JsonMappingException, JsonProcessingException;
	

}
