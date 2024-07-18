package com.hakmesb.lelabovert.payload;

public record CartDetailDto(
		Integer id,
		Integer quantity,
		Float amount,
		ProductDto productDto
		) {

}
