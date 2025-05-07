package com.hakmesb.lelabovert.payload.mapper;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.Account;
import com.hakmesb.lelabovert.payload.AccountDto;
import com.hakmesb.lelabovert.payload.CustomerDto;

@Service
public class AccountDtoMapper implements Function<Account, AccountDto>{

	private final CustomerDtoMapper customerDtoMapper;
	private final RoleDtoMapper roleDtoMapper;
	
	public AccountDtoMapper(CustomerDtoMapper customerDtoMapper, RoleDtoMapper roleDtoMapper) {
		this.customerDtoMapper = customerDtoMapper;
		this.roleDtoMapper = roleDtoMapper;
	}
	
	@Override
	public AccountDto apply(Account account) {
		CustomerDto customerDto = account.getCustomer() != null ? customerDtoMapper.apply(account.getCustomer()) : null;
		
		return new AccountDto(
			account.getId(),
			account.getEmail(),
			account.getRolesSet().stream().map(roleDtoMapper).collect(Collectors.toList()),
			Optional.ofNullable(customerDto),
			account.getCart().getId()
			);
	}

}
