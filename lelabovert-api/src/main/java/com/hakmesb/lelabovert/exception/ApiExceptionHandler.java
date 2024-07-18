package com.hakmesb.lelabovert.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hakmesb.lelabovert.payload.ApiResponse;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse> handleApiException(ApiException e){
		ApiResponse apiResponse = new ApiResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
		String message = e.getMessage();

		ApiResponse res = new ApiResponse(message, HttpStatus.NOT_FOUND);

		return new ResponseEntity<ApiResponse>(res, HttpStatus.NOT_FOUND);
	}
	
	
	
}
