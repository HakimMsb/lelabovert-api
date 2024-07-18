package com.hakmesb.lelabovert.payload.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.AlgeriaCity;
import com.hakmesb.lelabovert.payload.AlgeriaCityDto;

@Service
public class AlgeriaCityDtoMapper implements Function<AlgeriaCity, AlgeriaCityDto>{

	@Override
	public AlgeriaCityDto apply(AlgeriaCity algeriaCity) {
		return new AlgeriaCityDto(
				algeriaCity.getId(),
				algeriaCity.getCommuneName(),
				algeriaCity.getCommuneNameAscii(),
				algeriaCity.getDairaName(),
				algeriaCity.getDairaNameAscii(),
				algeriaCity.getWilayaCode(),
				algeriaCity.getWilayaName(),
				algeriaCity.getWilayaNameAscii()
				);
	}

}
