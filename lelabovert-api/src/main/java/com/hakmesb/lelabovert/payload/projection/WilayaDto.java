package com.hakmesb.lelabovert.payload.projection;

public class WilayaDto {

	private String wilayaNameAscii;

	private String wilayaCode;

	public WilayaDto(String wilayaNameAscii, String wilayaCode) {
		this.wilayaNameAscii = wilayaNameAscii;
		this.wilayaCode = wilayaCode;
	}

	public String getWilayaNameAscii() {
		return wilayaNameAscii;
	}

	public String getWilayaCode() {
		return wilayaCode;
	}

}
