package com.hakmesb.lelabovert.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hakmesb.lelabovert.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	Optional<Customer> findByAccountId(Integer accountId);
	
}
