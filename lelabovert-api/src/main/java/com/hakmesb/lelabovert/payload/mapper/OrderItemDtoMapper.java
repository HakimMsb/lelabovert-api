package com.hakmesb.lelabovert.payload.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.OrderItem;
import com.hakmesb.lelabovert.payload.OrderItemDto;

@Service
public class OrderItemDtoMapper implements Function<OrderItem, OrderItemDto>{

	private final ProductDtoMapper productDtoMapper;
	
	public OrderItemDtoMapper(ProductDtoMapper productDtoMapper) {
		this.productDtoMapper = productDtoMapper;
	}
	
	@Override
	public OrderItemDto apply(OrderItem orderItem) {
		return new OrderItemDto(
				orderItem.getId(),
				orderItem.getQuantity(),
				orderItem.getAmount(),
				productDtoMapper.apply(orderItem.getProduct())
				);
	}

}
