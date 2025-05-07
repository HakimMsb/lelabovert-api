package com.hakmesb.lelabovert.payload;

public record AddOrUpdateProductRequest(
		String name,
		String description,
		Float price
		) {

}
