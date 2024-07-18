package com.hakmesb.lelabovert.payload;

import java.util.Optional;

public record AddressDto(
		Integer id,
		Optional<String> homeAddress,
		AlgeriaCityDto algeriaCityDto
		) {

}
