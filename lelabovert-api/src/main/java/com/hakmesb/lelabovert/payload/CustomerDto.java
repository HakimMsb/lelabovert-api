package com.hakmesb.lelabovert.payload;

public record CustomerDto(
		Integer id,
		String firstName,
		String lastName,
		String email,
		String phoneNumber,
		AddressDto addressDto
		) {

}
