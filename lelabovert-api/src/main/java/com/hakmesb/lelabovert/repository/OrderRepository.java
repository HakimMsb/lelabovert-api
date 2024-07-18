package com.hakmesb.lelabovert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hakmesb.lelabovert.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	List<Order> findAllByCustomerId(Integer customerId);
	
	@Query("SELECT o FROM Order o JOIN o.customer c WHERE c.account.id = :accountId")
	List<Order> findByAccountId(Integer accountId);
	
	@Query("SELECT o FROM Order o JOIN o.customer c WHERE c.account.id = :accountId AND o.id = :id")
	Order findByIdAndAccountId(Integer id, Integer accountId);
	
}
