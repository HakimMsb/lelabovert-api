package com.hakmesb.lelabovert.payload;

import java.util.Optional;

public record ProductDto(
		Integer id,
		String name,
		String slug,
		Optional<String> description,
		Float price,
		Optional<String> image,
		Integer quantity
		) {

}
