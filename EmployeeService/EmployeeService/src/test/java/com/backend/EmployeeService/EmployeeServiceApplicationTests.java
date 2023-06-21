package com.backend.EmployeeService;
import com.backend.EmployeeService.Entity.Tax;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.backend.EmployeeService.Entity.Employee;
import com.backend.EmployeeService.Entity.Leaves;
import com.backend.EmployeeService.Repository.EmployeeRepository;
import com.backend.EmployeeService.Service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class EmployeeServiceApplicationTests {
	
	@Mock
    private EmployeeRepository empRepo;
	
	@Mock
    private PasswordEncoder encoder;
	
	@Mock
    private RestTemplate restTemplate;
	
	@Mock
    private ObjectMapper objectMapper;
	
    
    @InjectMocks
    private EmployeeService employeeService;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

	@Test
	void contextLoads() {
	}
	
	@Test
    public void testGetAllEmployee() {
		List<Employee> dummyEmployees = new ArrayList<>();
        dummyEmployees.add(new Employee(1,"Debanka","User@1234",22,"Chakdah","User"));
        dummyEmployees.add(new Employee(2,"James","Admin@1234",42,"London","Admin"));
        
        when(empRepo.findAll()).thenReturn(dummyEmployees);
        
        List<Employee> employees = employeeService.getAllEmployee();
        
        assertEquals(2, employees.size());   
	}
	
	@Test
    public void testGetEmployeeById() {

        Employee dummyEmployee = new Employee(1,"Debanka","User@1234",22,"Chakdah","User");


        when(empRepo.findById(1)).thenReturn(Optional.of(dummyEmployee));


        ResponseEntity<Optional<Employee>> response = employeeService.getEmployeeById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dummyEmployee, response.getBody().orElse(null));
    }
	
	@Test
    public void testGetEmployeeByName() {
        
        Employee dummyEmployee =  new Employee(1,"Debanka","User@1234",22,"Chakdah","User");


        when(empRepo.getEmployeeByName("Debanka")).thenReturn(dummyEmployee);

        Optional<Employee> employeeOptional = employeeService.getEmployeeByName("Debanka");

        assertTrue(employeeOptional.isPresent());
        assertEquals(dummyEmployee, employeeOptional.get());
    }
	
	@Test
    void testSaveEmployee() {
		
		Employee dummyEmployee =  new Employee(1,"Debanka","User@1234",22,"Chakdah","User");

        when(encoder.encode(dummyEmployee.getPassword())).thenReturn("User@1234");

        
        ResponseEntity<Employee> response = employeeService.saveEmployee(dummyEmployee);

       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dummyEmployee, response.getBody());
        verify(empRepo, times(1)).save(dummyEmployee);
        verify(encoder, times(1)).encode(dummyEmployee.getPassword());
    }
	
	
	@Test
    void tesrUpdateEmployee() {
 
        Integer id = 1;
        Employee existingEmployee = new Employee(1,"Debanka","User@1234",22,"Chakdah","User");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setName("Shubham");
        updatedEmployee.setAddress("Behala");
        updatedEmployee.setAge(35);

        Optional<Employee> optionalEmployee = Optional.of(existingEmployee);

        when(empRepo.findById(id)).thenReturn(optionalEmployee);
        when(empRepo.save(existingEmployee)).thenReturn(existingEmployee);

       
        ResponseEntity<Employee> response = employeeService.updateEmployee(updatedEmployee, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingEmployee, response.getBody());
        assertEquals(updatedEmployee.getName(), existingEmployee.getName());
        assertEquals(updatedEmployee.getAddress(), existingEmployee.getAddress());
        assertEquals(updatedEmployee.getAge(), existingEmployee.getAge());
        verify(empRepo, times(1)).findById(id);
        verify(empRepo, times(1)).save(existingEmployee);
    }
	
	@Test
    void testDeleteEmployee() {
        Integer id = 1;
        
        ResponseEntity<String> response = employeeService.deleteEmployee(id);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee of id " + id + " is deleted", response.getBody());
        verify(empRepo, times(1)).deleteById(id);
    }
	
	@Test
	void testGetAllLeavesByUserId() {
		Integer id = 1;
        String token = "dummyToken";
        Employee existingEmployee = new Employee();
        existingEmployee.setId(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        
        List<Leaves> mockLeaves = Arrays.asList(new Leaves(), new Leaves());

        ResponseEntity<List> mockResponse = new ResponseEntity<>(mockLeaves, HttpStatus.OK);

        Optional<Employee> optionalEmployee = Optional.of(existingEmployee);

        when(empRepo.findById(id)).thenReturn(optionalEmployee);
        when(restTemplate.exchange(
                "http://LEAVES-SERVICE:8092/leaves/" + existingEmployee.getId(),
                HttpMethod.GET,
                requestEntity,
                List.class
        )).thenReturn(mockResponse);
        
        ResponseEntity<Employee> response = employeeService.getAllLeavesByUserId(id, token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingEmployee, response.getBody());
        assertEquals(mockLeaves, existingEmployee.getAllLeaves());
        verify(empRepo, times(1)).findById(id);
        verify(restTemplate, times(1)).exchange(
                "http://LEAVES-SERVICE:8092/leaves/" + existingEmployee.getId(),
                HttpMethod.GET,
                requestEntity,
                List.class
        );
        
        
	}
	
//	@Test
//	void testGetTaxByEmpId() throws JsonMappingException, JsonProcessingException {
//		Integer id = 1;
//        String token = "dummyToken";
//        Employee existingEmployee = new Employee();
//        existingEmployee.setId(id);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", token);
//        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//        String mockTaxResponse = "{\"taxId\":1,\"taxAmount\":80000,\"empId\":1,\"taxPerc\":10,\"inHand\":720000,\"salary\":800000}";
//
//        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockTaxResponse, HttpStatus.OK);
//
//        Optional<Employee> optionalEmployee = Optional.of(existingEmployee);
//        
//        when(empRepo.findById(id)).thenReturn(optionalEmployee);
//        when(restTemplate.exchange(
//                "http://TAX-SERVICE:8093/tax/find/" + existingEmployee.getId(),
//                HttpMethod.GET,
//                requestEntity,
//                String.class
//        )).thenReturn(mockResponse);
//
//        Tax mockTax = new Tax();
//        mockTax.setTaxId(1);
//        mockTax.setTaxAmount(80000);
//        mockTax.setInHand(720000);
//        mockTax.setSalary(800000);
//        mockTax.setTaxPerc(10);
//        mockTax.setEmpId(1);
//
//        when(objectMapper.readValue(mockTaxResponse, Tax.class)).thenReturn(mockTax);
//        
//        ResponseEntity<Tax> response = employeeService.getTaxByEmpId(id, token);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(mockTax, response.getBody());
//        verify(empRepo, times(1)).findById(id);
//        verify(restTemplate, times(1)).exchange(
//                "http://TAX-SERVICE:8093/tax/find/" + existingEmployee.getId(),
//                HttpMethod.GET,
//                requestEntity,
//                String.class
//        );
//        verify(objectMapper, times(1)).readValue(mockTaxResponse, Tax.class);
//
//	}
//

}
