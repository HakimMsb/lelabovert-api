package com.hakmesb.lelabovert.payload;

public record OrderItemDto(
		Integer id,
		Integer quantity,
		Float amount,
		ProductDto product
		) {

}
