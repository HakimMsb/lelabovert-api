package com.hakmesb.lelabovert.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hakmesb.lelabovert.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	Category findByName(String name);
	
	Optional<Category> findBySlug(String slug);
}
