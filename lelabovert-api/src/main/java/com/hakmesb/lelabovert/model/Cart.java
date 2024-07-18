package com.hakmesb.lelabovert.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * The persistent class for the cart database table.
 * 
 */
@Entity
@NoArgsConstructor
@Data
@ToString
@NamedQuery(name="Cart.findAll", query="SELECT c FROM Cart c")
public class Cart implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="total_amount")
	private float totalAmount;
	
	//bi-directional one-to-one association to Account
	@OneToOne
	@JoinColumn(name = "accountId")
	private Account account;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", insertable=false, updatable=false)
	private Date createdAt;
	
	//bi-directional many-to-one association to CartDetail
	@OneToMany(mappedBy="cart", cascade=CascadeType.ALL)
	private List<CartDetail> cartDetails;

	public CartDetail addCartDetail(CartDetail cartDetail) {
		getCartDetails().add(cartDetail);
		cartDetail.setCart(this);

		return cartDetail;
	}

	public CartDetail removeCartDetail(CartDetail cartDetail) {
		getCartDetails().remove(cartDetail);
		cartDetail.setCart(null);

		return cartDetail;
	}

}