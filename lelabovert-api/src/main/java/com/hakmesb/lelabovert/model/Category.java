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
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * The persistent class for the category database table.
 * 
 */
@Entity
@NoArgsConstructor
@Data
@ToString
@NamedQuery(name="Category.findAll", query="SELECT c FROM Category c")
public class Category implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private String slug;
	
	private String description;
	
	private String image;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", insertable=false, updatable=false)
	private Date createdAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at", insertable=false, updatable=false)
	private Date updatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="deleted_at", insertable=false)
	private Date deletedAt;
	
	// bi-directional many-to-one association to Product
	@OneToMany(mappedBy="category", cascade=CascadeType.ALL)
	private List<Product> products;

}