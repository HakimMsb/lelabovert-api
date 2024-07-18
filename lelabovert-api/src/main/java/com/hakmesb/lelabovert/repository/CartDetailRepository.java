package com.hakmesb.lelabovert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.hakmesb.lelabovert.model.CartDetail;

public interface CartDetailRepository extends JpaRepository<CartDetail, Integer>{

	CartDetail findByProductIdAndCartId(Integer productId, Integer cartId);
	
	List<CartDetail> findByCartId(Integer cartId);
	
	@Modifying
	@Query("DELETE FROM CartDetail cd WHERE cd.product.id = :productId AND cd.cart.id = :cartId")
	void deleteByProductIdAndCartId(Integer productId, Integer cartId);
}
