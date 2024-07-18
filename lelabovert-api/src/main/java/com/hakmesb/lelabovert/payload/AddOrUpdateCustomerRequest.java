package com.hakmesb.lelabovert.payload;

import java.util.Optional;

public record AddOrUpdateCustomerRequest(
		String email,
		String firstName,
		String lastName,
		String phoneNumber,
		String homeAddress,
		Integer commune,
		Optional<Integer> accountId
		) {

}
