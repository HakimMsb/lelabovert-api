package com.hakmesb.lelabovert.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * The persistent class for the algeria_cities database table.
 * 
 */
@Entity
@NoArgsConstructor
@Data
@ToString
@Table(name="algeria_cities")
@NamedQuery(name="AlgeriaCity.findAll", query="SELECT a FROM AlgeriaCity a")
public class AlgeriaCity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="commune_name")
	private String communeName;

	@Column(name="commune_name_ascii")
	private String communeNameAscii;

	@Column(name="daira_name")
	private String dairaName;

	@Column(name="daira_name_ascii")
	private String dairaNameAscii;

	@Column(name="wilaya_code")
	private String wilayaCode;

	@Column(name="wilaya_name")
	private String wilayaName;

	@Column(name="wilaya_name_ascii")
	private String wilayaNameAscii;

	//bi-directional many-to-one association to Address
	@OneToMany(mappedBy="algeriaCity")
	private List<Address> addresses;

	public Address addAddress(Address address) {
		getAddresses().add(address);
		address.setAlgeriaCity(this);

		return address;
	}

	public Address removeAddress(Address address) {
		getAddresses().remove(address);
		address.setAlgeriaCity(null);

		return address;
	}

}