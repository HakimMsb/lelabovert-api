package com.hakmesb.lelabovert.payload;

import java.util.List;

public record OrderResponse(
		List<OrderDto> orderDtos,
		Integer pageNumber,
		Integer pageSize,
		Long totalElements,
		Integer totalPages,
		boolean lastPage
		) {

}
