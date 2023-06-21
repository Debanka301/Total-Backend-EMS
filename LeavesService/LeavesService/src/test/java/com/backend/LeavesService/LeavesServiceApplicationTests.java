package com.backend.LeavesService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.backend.LeavesService.Entity.Leaves;
import com.backend.LeavesService.Repository.LeavesRepository;
import com.backend.LeavesService.Service.LeavesServices;

@SpringBootTest
class LeavesServiceApplicationTests {
	
	@Mock
    private RestTemplate restTemplate;

    @Mock
    private LeavesRepository leavesRepo;

    @InjectMocks
    private LeavesServices leavesService;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

	@Test
	void contextLoads() {
	}
	
	@Test
    void testGetLeaveByUserId() {
		Integer userId = 1;
        String token = "dummyToken";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> mockResponse = new ResponseEntity<>("valid", HttpStatus.OK);
        
        when(restTemplate.exchange(
                "http://EMPLOYEE-SERVICE:8091/validateToken",
                HttpMethod.GET,
                requestEntity,
                String.class
        )).thenReturn(mockResponse);

        List<Leaves> mockLeaves = List.of(new Leaves(), new Leaves());

        when(leavesRepo.getLeavesByEmployeeId(userId)).thenReturn(mockLeaves);

        // Act
        List<Leaves> result = leavesService.getLeaveByUserId(userId, token);

        assertNotNull(result);
        assertEquals(mockLeaves, result);
        verify(restTemplate, times(1)).exchange(
                "http://EMPLOYEE-SERVICE:8091/validateToken",
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        verify(leavesRepo, times(1)).getLeavesByEmployeeId(userId);
	}
	
	@Test
    void testSaveLeaves() {
		Leaves leaves = new Leaves();
        String token = "dummyToken";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> mockResponse = new ResponseEntity<>("valid", HttpStatus.OK);
        
        when(restTemplate.exchange(
                "http://EMPLOYEE-SERVICE/validateToken",
                HttpMethod.GET,
                requestEntity,
                String.class
        )).thenReturn(mockResponse);

        // Act
        ResponseEntity<Leaves> result = leavesService.saveLeaves(leaves, token);
        
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(leaves, result.getBody());
        verify(restTemplate, times(1)).exchange(
                "http://EMPLOYEE-SERVICE/validateToken",
                HttpMethod.GET,
                requestEntity,
                String.class
        );
        verify(leavesRepo, times(1)).save(leaves);
	}

}
