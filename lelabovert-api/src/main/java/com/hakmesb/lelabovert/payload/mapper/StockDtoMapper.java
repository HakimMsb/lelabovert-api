package com.hakmesb.lelabovert.payload.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.Stock;
import com.hakmesb.lelabovert.payload.StockDto;

@Service
public class StockDtoMapper implements Function<Stock, StockDto>{

	@Override
	public StockDto apply(Stock stock) {
		return new StockDto(
				stock.getId(),
				stock.getQuantity(),
				stock.getProduct().getName()
				);
	}

}
