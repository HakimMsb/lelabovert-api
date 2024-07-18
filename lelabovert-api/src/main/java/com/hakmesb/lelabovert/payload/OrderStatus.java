package com.hakmesb.lelabovert.payload;

public record OrderStatus(
		Boolean isConfirmed,
		Boolean isDispatched
		) {

}
