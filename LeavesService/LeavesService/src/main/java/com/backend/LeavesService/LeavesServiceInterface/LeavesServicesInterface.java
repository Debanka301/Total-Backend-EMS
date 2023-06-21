package com.backend.LeavesService.LeavesServiceInterface;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.backend.LeavesService.Entity.Leaves;

public interface LeavesServicesInterface {
	
	public List<Leaves> getLeaveByUserId(Integer userId, String token);
	
	public ResponseEntity<Leaves> saveLeaves(Leaves leaves, String token);

}
