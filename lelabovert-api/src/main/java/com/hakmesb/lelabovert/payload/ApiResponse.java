package com.hakmesb.lelabovert.payload;

import org.springframework.http.HttpStatus;

public record ApiResponse(
		String message,
		HttpStatus httpStatus
		){
	
}
