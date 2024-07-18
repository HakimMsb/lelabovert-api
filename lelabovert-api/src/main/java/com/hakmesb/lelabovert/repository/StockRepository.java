package com.hakmesb.lelabovert.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hakmesb.lelabovert.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Integer>{
	
}
