package com.hakmesb.lelabovert.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hakmesb.lelabovert.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Integer>{

	void deleteByOrderId(Integer orderId);
	
}
