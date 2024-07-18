package com.hakmesb.lelabovert.payload.mapper;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.Category;
import com.hakmesb.lelabovert.payload.CategoryDto;

@Service
public class CategoryDtoMapper implements Function<Category, CategoryDto>{

	@Override
	public CategoryDto apply(Category category) {
		return new CategoryDto(
				category.getId(),
				category.getName(),
				category.getSlug(),
				Optional.ofNullable(category.getDescription()),
				Optional.ofNullable(category.getImage())
				);
	}

}
