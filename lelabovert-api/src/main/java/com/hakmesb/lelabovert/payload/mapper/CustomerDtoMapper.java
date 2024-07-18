package com.hakmesb.lelabovert.payload.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.Customer;
import com.hakmesb.lelabovert.payload.CustomerDto;

@Service
public class CustomerDtoMapper implements Function<Customer, CustomerDto>{
	
	private final AddressDtoMapper addressDtoMapper;
	
	public CustomerDtoMapper(AddressDtoMapper addressDtoMapper) {
		this.addressDtoMapper = addressDtoMapper;
	}

	@Override
	public CustomerDto apply(Customer customer) {
		return new CustomerDto(
				customer.getId(),
				customer.getFirstName(),
				customer.getLastName(),
				customer.getEmail(),
				customer.getPhoneNumber(),
				addressDtoMapper.apply(customer.getAddress())
				);
	}

}