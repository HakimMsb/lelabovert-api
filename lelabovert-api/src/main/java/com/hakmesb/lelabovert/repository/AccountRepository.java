package com.hakmesb.lelabovert.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hakmesb.lelabovert.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{
	
	Optional<Account> findByEmail(String email);
	
}
