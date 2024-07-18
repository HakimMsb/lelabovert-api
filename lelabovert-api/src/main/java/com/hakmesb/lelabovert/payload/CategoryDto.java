package com.hakmesb.lelabovert.payload;

import java.util.Optional;

public record CategoryDto (
		Integer id,
		String name,
		String slug,
		Optional<String> description,
		Optional<String> image
		) {
	
}
