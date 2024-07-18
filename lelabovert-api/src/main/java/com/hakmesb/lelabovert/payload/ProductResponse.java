package com.hakmesb.lelabovert.payload;

import java.util.List;

public record ProductResponse(
		List<ProductDto> products,
		Integer pageNumber,
		Integer pageSize,
		Long totalElements,
		Integer totalPages,
		boolean lastPage
		) {

}
