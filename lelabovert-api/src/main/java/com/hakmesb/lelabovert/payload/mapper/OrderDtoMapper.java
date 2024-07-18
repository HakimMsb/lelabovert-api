package com.hakmesb.lelabovert.payload.mapper;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.Order;
import com.hakmesb.lelabovert.payload.OrderDto;

@Service
public class OrderDtoMapper implements Function<Order, OrderDto>{
	
	private final CustomerDtoMapper customerDtoMapper;
	private final OrderItemDtoMapper orderItemDtoMapper;
	
	public OrderDtoMapper(CustomerDtoMapper customerDtoMapper, OrderItemDtoMapper orderItemDtoMapper) {
		this.customerDtoMapper = customerDtoMapper;
		this.orderItemDtoMapper = orderItemDtoMapper;
	}

	@Override
	public OrderDto apply(Order order) {
		return new OrderDto(
				order.getId(),
				order.getTotalAmount(),
				customerDtoMapper.apply(order.getCustomer()),
				order.getOrderItems().stream().map(orderItemDtoMapper).collect(Collectors.toList()),
				order.isConfirmed(),
				order.isDispatched(),
				order.getCreatedAt(),
				order.getUpdatedAt(),
				order.getDeletedAt()
				);
	}

}
