package com.hakmesb.lelabovert.payload.mapper;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.Cart;
import com.hakmesb.lelabovert.payload.CartDto;

@Service
public class CartDtoMapper implements Function<Cart, CartDto>{

	private final CartDetailDtoMapper cartDetailDtoMapper;
	
	public CartDtoMapper(CartDetailDtoMapper cartDetailDtoMapper) {
		this.cartDetailDtoMapper = cartDetailDtoMapper;
	}
	
	@Override
	public CartDto apply(Cart cart) {
		return new CartDto(
				cart.getId(),
				cart.getTotalAmount(),
				cart.getCartDetails().stream().map(cartDetailDtoMapper).collect(Collectors.toList())
				);
	}

}
