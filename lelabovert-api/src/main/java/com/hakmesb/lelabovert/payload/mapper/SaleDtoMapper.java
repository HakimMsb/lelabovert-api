package com.hakmesb.lelabovert.payload.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.Sale;
import com.hakmesb.lelabovert.payload.SaleDto;

@Service
public class SaleDtoMapper implements Function<Sale, SaleDto>{

	@Override
	public SaleDto apply(Sale sale) {
		return new SaleDto(
				sale.getId(),
				sale.getQuantity(),
				sale.getAmount(),
				sale.getProduct().getName(),
				sale.getOrder().getId()
				);
	}

}
