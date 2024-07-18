package com.hakmesb.lelabovert.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hakmesb.lelabovert.payload.AuthenticationResponse;
import com.hakmesb.lelabovert.payload.LoginRequest;
import com.hakmesb.lelabovert.payload.RegistrationRequest;
import com.hakmesb.lelabovert.service.AuthenticationService;

@RequestMapping("/api/v1")
@RestController
public class AuthenticationController {
	
	private final AuthenticationService authenticationService;
	
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest request){
		return ResponseEntity.ok(authenticationService.register(request));
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request){
		return ResponseEntity.ok(authenticationService.authenticate(request));
	}
	
	@GetMapping("/logout")
	public void logout() {
		
	}
	
}
