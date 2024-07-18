package com.hakmesb.lelabovert.payload;

public record AlgeriaCityDto(
		Integer id,
		String communeName,
		String communeNameAscii,
		String dairaName,
		String dairaNameAscii,
		String wilayaCode,
		String wilayaName,
		String wilayaNameAscii
		) {

}
