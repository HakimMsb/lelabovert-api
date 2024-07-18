package com.hakmesb.lelabovert.payload;

import java.util.List;
import java.util.Optional;

import com.hakmesb.lelabovert.model.Role;

public record RegistrationRequest(
		String email,
		String password,
		Optional<List<Role>> roles,
		Optional<Integer> customerId 
		) {

}
