package com.hakmesb.lelabovert.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * The persistent class for the cart_details database table.
 * 
 */
@Entity
@NoArgsConstructor
@Data
@ToString
@Table(name="cart_details")
@NamedQuery(name="CartDetail.findAll", query="SELECT c FROM CartDetail c")
public class CartDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private int quantity;

	private float amount;

	//bi-directional many-to-one association to Cart
	@ManyToOne
	private Cart cart;

	//bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

}