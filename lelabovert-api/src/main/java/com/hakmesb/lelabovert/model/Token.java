package com.hakmesb.lelabovert.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Token {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String token;
	
	@Column(name="is_logged_out")
	private boolean loggedOut;
	
	@ManyToOne
	@JoinColumn(name="account_id")
	private Account account;

}
