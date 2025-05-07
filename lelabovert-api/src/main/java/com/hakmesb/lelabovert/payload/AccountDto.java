package com.hakmesb.lelabovert.payload;

import java.util.List;
import java.util.Optional;

public record AccountDto(
		Integer id,
		String email,
		List<RoleDto> rolesSet,
		Optional<CustomerDto> customerDto,
		Integer cartId
		) {

}
