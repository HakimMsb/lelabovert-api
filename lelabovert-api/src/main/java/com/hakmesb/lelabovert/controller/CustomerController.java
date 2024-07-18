package com.hakmesb.lelabovert.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hakmesb.lelabovert.model.Account;
import com.hakmesb.lelabovert.payload.AddOrUpdateCustomerRequest;
import com.hakmesb.lelabovert.payload.AddOrUpdateCustomerResponse;
import com.hakmesb.lelabovert.payload.CustomerDto;
import com.hakmesb.lelabovert.service.CustomerService;

@RequestMapping("/api/v1")
@RestController
public class CustomerController {
	
	private final CustomerService customerService;
	
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	@PostMapping("/public/customer")
	public ResponseEntity<CustomerDto> addOrUpdateCustomer(@RequestBody AddOrUpdateCustomerRequest request, Authentication authentication){
		AddOrUpdateCustomerResponse response;
		
		if (authentication != null && authentication.isAuthenticated()) {
			response = customerService.addOrUpdateCustomer(request, (Account) authentication.getPrincipal());
		}else {
			response = customerService.addOrUpdateCustomer(request, null);
		}
		
		HttpStatus status = response.isCreated() ? HttpStatus.CREATED : HttpStatus.OK;
		
		return new ResponseEntity<CustomerDto>(response.customerDto(), status);
	}

}
