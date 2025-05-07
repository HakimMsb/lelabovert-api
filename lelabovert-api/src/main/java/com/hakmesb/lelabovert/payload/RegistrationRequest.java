package com.hakmesb.lelabovert.payload;

import java.util.List;
import java.util.Optional;

public record RegistrationRequest(
		String email,
		String password,
		Optional<List<RoleDto>> roles,
		Optional<Integer> customerId 
		) {

}
