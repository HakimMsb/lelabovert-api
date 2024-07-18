package com.hakmesb.lelabovert.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hakmesb.lelabovert.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{

}
