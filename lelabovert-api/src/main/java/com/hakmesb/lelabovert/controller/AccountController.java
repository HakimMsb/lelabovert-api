package com.hakmesb.lelabovert.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hakmesb.lelabovert.config.AppConstants;
import com.hakmesb.lelabovert.model.Account;
import com.hakmesb.lelabovert.payload.AccountDto;
import com.hakmesb.lelabovert.payload.AccountResponse;
import com.hakmesb.lelabovert.payload.ChangePasswordRequest;
import com.hakmesb.lelabovert.service.AccountDetailsService;

@RequestMapping("/api/v1")
@RestController
public class AccountController {

	private final AccountDetailsService accountDetailsService;
	
	public AccountController(AccountDetailsService accountDetailsService) {
		this.accountDetailsService = accountDetailsService;
	}
	
	@GetMapping("/admin/accounts")
	public ResponseEntity<AccountResponse> getAllAccounts(
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
			@RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
		AccountResponse accountResponse = accountDetailsService.getAllAccounts(pageNumber, pageSize, sortBy, sortOrder);
		
		return new ResponseEntity<AccountResponse>(accountResponse, HttpStatus.FOUND);
	}
	
	@GetMapping("/user/account")
	public ResponseEntity<AccountDto> getAuthenticatedAccount(Authentication authentication){
		Account account = (Account) authentication.getPrincipal();
		
		AccountDto accountDto = accountDetailsService.getAccountById(account.getId());
		
		return new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK);
	}
	
	@PutMapping("/user/account/password")
	public ResponseEntity<AccountDto> changePassword(@RequestBody ChangePasswordRequest request,
			Authentication authentication){
		Account account = (Account) authentication.getPrincipal();
		
		AccountDto accountDto = accountDetailsService.changePassword(account.getId(), request.oldPassword(), request.newPassword());
		
		return new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/admin/accounts/{accountId}")
	public ResponseEntity<String> deleteAccount(@PathVariable Integer accountId){
		String status = accountDetailsService.deleteAccount(accountId);
		
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
	
}
