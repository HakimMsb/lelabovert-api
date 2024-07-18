package com.hakmesb.lelabovert.payload;

import java.util.Date;
import java.util.List;

public record OrderDto(
		Integer id,
		Float totalAmount,
		CustomerDto customerDto,
		List<OrderItemDto> orderItemDtos,
		Boolean isConfirmed,
		Boolean isDispatches,
		Date createdAt,
		Date updatedAt,
		Date deletedAt
		) {

}
