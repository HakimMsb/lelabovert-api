package com.hakmesb.lelabovert.payload;

public record AddOrUpdateCustomerResponse(
		CustomerDto customerDto,
		Boolean isCreated
		) {

}
