package com.hakmesb.lelabovert.payload;

import java.util.List;

public record CartDto(
		Integer id,
		Float totalAmount,
		List<CartDetailDto> cartDetailDtos
		) {

}
