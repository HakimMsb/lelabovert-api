package com.hakmesb.lelabovert.payload;

public record SaleDto(
		Integer id,
		Integer quantity,
		Float amount,
		String productName,
		Integer orderId
		) {

}
