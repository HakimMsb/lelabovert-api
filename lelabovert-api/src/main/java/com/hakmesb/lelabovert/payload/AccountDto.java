package com.hakmesb.lelabovert.payload;

import java.util.List;
import java.util.Optional;

import com.hakmesb.lelabovert.model.Role;

public record AccountDto(
		Integer id,
		String email,
		List<Role> rolesSet,
		Optional<CustomerDto> customerDto
		) {

}
