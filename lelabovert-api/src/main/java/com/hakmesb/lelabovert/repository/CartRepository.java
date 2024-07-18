package com.hakmesb.lelabovert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hakmesb.lelabovert.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>{

	Cart findByAccountId(Integer accountId);
	
	@Query("SELECT c FROM Cart c JOIN c.cartDetails cd WHERE cd.product.id = :productId")
	List<Cart> findByProductId(Integer productId);
	
	Cart findByAccountIdAndId(Integer accountId, Integer id);
}
