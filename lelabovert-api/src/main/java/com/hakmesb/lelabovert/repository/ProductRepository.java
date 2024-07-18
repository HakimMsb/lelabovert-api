package com.hakmesb.lelabovert.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hakmesb.lelabovert.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

	Page<Product> findByNameLike(String keyword, Pageable pageDetails);
	
	Page<Product> findByCategoryId(Integer categoryId, Pageable pageDetails);
	
	Optional<Product> findBySlug(String slug);
}
