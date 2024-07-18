package com.hakmesb.lelabovert.payload.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.CartDetail;
import com.hakmesb.lelabovert.payload.CartDetailDto;

@Service
public class CartDetailDtoMapper implements Function<CartDetail, CartDetailDto>{

	private final ProductDtoMapper productDtoMapper;
	
	public CartDetailDtoMapper(ProductDtoMapper productDtoMapper) {
		this.productDtoMapper = productDtoMapper;
	}
	
	@Override
	public CartDetailDto apply(CartDetail cartDetail) {
		return new CartDetailDto(
				cartDetail.getId(),
				cartDetail.getQuantity(),
				cartDetail.getAmount(),
				productDtoMapper.apply(cartDetail.getProduct())
				);
	}

}
