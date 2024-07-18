package com.hakmesb.lelabovert.payload.mapper;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.Address;
import com.hakmesb.lelabovert.payload.AddressDto;

@Service
public class AddressDtoMapper implements Function<Address, AddressDto>{
	
	private final AlgeriaCityDtoMapper algeriaCityDtoMapper;
	
	public AddressDtoMapper(AlgeriaCityDtoMapper algeriaCityDtoMapper) {
		this.algeriaCityDtoMapper = algeriaCityDtoMapper;
	}

	@Override
	public AddressDto apply(Address address) {
		return new AddressDto(
				address.getId(),
				Optional.ofNullable(address.getHomeAddress()),
				algeriaCityDtoMapper.apply(address.getAlgeriaCity())
				);
	}

}
