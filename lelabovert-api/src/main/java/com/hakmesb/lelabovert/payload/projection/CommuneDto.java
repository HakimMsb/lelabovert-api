package com.hakmesb.lelabovert.payload.projection;

public class CommuneDto {

	private Integer id;

	private String communeNameAscii;

	public CommuneDto(Integer id, String communeNameAscii) {
		this.id = id;
		this.communeNameAscii = communeNameAscii;
	}

	public Integer getId() {
		return id;
	}

	public String getCommuneNameAscii() {
		return communeNameAscii;
	}

}
