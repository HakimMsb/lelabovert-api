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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * The persistent class for the product database table.
 * 
 */
@Entity
@NoArgsConstructor
@Data
@ToString
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String slug;

	private float price;
	
	private String description;
	
	private String image;
	
	@Column(insertable=false, updatable=false)
	private int quantity;

	@ManyToOne
	@JoinColumn(name="category_id")
	private Category category;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", insertable=false, updatable=false)
	private Date createdAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at", insertable=false, updatable=false)
	private Date updatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="deleted_at", insertable=false)
	private Date deletedAt;

	//bi-directional many-to-one association to OrderItem
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL)
	private List<OrderItem> orderItems;

	//bi-directional many-to-one association to Sale
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL)
	private List<Sale> sales;

	//bi-directional many-to-one association to Stock
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL)
	private List<Stock> stocks;
	
	//bi-directional many-to-one association to CartDetail
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL)
	private List<CartDetail> cartDetails;

	public OrderItem addOrderItem(OrderItem orderItem) {
		getOrderItems().add(orderItem);
		orderItem.setProduct(this);

		return orderItem;
	}

	public OrderItem removeOrderItem(OrderItem orderItem) {
		getOrderItems().remove(orderItem);
		orderItem.setProduct(null);

		return orderItem;
	}

	public Sale addSale(Sale sale) {
		getSales().add(sale);
		sale.setProduct(this);

		return sale;
	}

	public Sale removeSale(Sale sale) {
		getSales().remove(sale);
		sale.setProduct(null);

		return sale;
	}

	public Stock addStock(Stock stock) {
		getStocks().add(stock);
		stock.setProduct(this);

		return stock;
	}

	public Stock removeStock(Stock stock) {
		getStocks().remove(stock);
		stock.setProduct(null);

		return stock;
	}
	
	public CartDetail addCartDetail(CartDetail cartDetail) {
		getCartDetails().add(cartDetail);
		cartDetail.setProduct(this);

		return cartDetail;
	}

	public CartDetail removeCartDetail(CartDetail cartDetail) {
		getCartDetails().remove(cartDetail);
		cartDetail.setProduct(null);

		return cartDetail;
	}

}