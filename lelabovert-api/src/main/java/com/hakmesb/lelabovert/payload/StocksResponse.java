package com.hakmesb.lelabovert.payload;

import java.util.List;

public record StocksResponse(
		List<StockDto> stockDtos,
		Integer pageNumber,
		Integer pageSize,
		Long totalElements,
		Integer totalPages,
		boolean lastPage
		) {

}
