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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * The persistent class for the order database table.
 * 
 */
@Entity
@NoArgsConstructor
@Data
@ToString
@Table(name="`order`")
@NamedQuery(name="Order.findAll", query="SELECT o FROM Order o")
public class Order implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="total_amount")
	private float totalAmount;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@Column(name="is_confirmed")
	private boolean isConfirmed;
	
	@Column(name="is_dispatched")
	private boolean isDispatched;

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
	@OneToMany(mappedBy="order", cascade=CascadeType.ALL)
	private List<OrderItem> orderItems;

	//bi-directional many-to-one association to Sale
	@OneToMany(mappedBy="order")
	private List<Sale> sales;

	public OrderItem addOrderItem(OrderItem orderItem) {
		getOrderItems().add(orderItem);
		orderItem.setOrder(this);

		return orderItem;
	}

	public OrderItem removeOrderItem(OrderItem orderItem) {
		getOrderItems().remove(orderItem);
		orderItem.setOrder(null);

		return orderItem;
	}

	public Sale addSale(Sale sale) {
		getSales().add(sale);
		sale.setOrder(this);

		return sale;
	}

	public Sale removeSale(Sale sale) {
		getSales().remove(sale);
		sale.setOrder(null);

		return sale;
	}

}