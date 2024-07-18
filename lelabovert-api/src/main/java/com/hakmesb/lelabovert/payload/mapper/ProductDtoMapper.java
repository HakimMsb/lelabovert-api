package com.hakmesb.lelabovert.payload.mapper;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.Product;
import com.hakmesb.lelabovert.payload.ProductDto;

@Service
public class ProductDtoMapper implements Function<Product, ProductDto>{

	@Override
	public ProductDto apply(Product product) {
		return new ProductDto(
				product.getId(),
				product.getName(),
				product.getSlug(),
				Optional.ofNullable(product.getDescription()),
				product.getPrice(),
				Optional.ofNullable(product.getImage()),
				product.getQuantity()
				);
	}

}
