package com.hakmesb.lelabovert.payload;

import java.util.List;

public record SalesResponse(
		List<SaleDto> saleDtos,
		Integer pageNumber,
		Integer pageSize,
		Long totalElements,
		Integer totalPages,
		boolean lastPage
		) {

}
