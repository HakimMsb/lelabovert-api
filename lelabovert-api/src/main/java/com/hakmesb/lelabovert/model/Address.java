package com.hakmesb.lelabovert.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The persistent class for the address database table.
 * 
 */
@Entity
@NoArgsConstructor
@Data
@ToString
@NamedQuery(name = "Address.findAll", query = "SELECT a FROM Address a")
public class Address implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "home_address")
	private String homeAddress;
	
	// bi-directional many-to-one association to AlgeriaCity
	@ManyToOne
	@JoinColumn(name = "algeria_cities_id")
	private AlgeriaCity algeriaCity;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", insertable=false, updatable=false)
	private Date createdAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at", insertable=false, updatable=false)
	private Date updatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at", insertable=false)
	private Date deletedAt;

	// bi-directional one-to-one association to Customer
	@OneToOne(mappedBy = "address")
	private Customer customer;

}