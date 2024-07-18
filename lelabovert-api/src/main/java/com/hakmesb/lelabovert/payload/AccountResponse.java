package com.hakmesb.lelabovert.payload;

import java.util.List;

public record AccountResponse(
		List<AccountDto> accounts,
		Integer pageNumber,
		Integer pageSize,
		Long totalElements,
		Integer totalPages,
		boolean lastPage
		) {

}
