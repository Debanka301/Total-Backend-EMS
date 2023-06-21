package com.backend.EmployeeService.EmployeeServiceInterfaces;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JwtServiceInterface {
	
	public String extractUsername(String token);
	
	public Date extractExpiration(String token);
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
	
	private Boolean isTokenExpired(String token) {
		return null;
	}
	
	private Claims extractAllClaims(String token) {
		return null;
	}
	
	public Boolean validateToken(String token, UserDetails userDetails);
	
	public String isTokenValid(String token);
	
	public String generateToken(String userName);
	
	private String createToken(Map<String, Object> claims, String userName) {
		return null;
	}
	
	private Key getSignKey() {
		return null;
	}

}
