package com.hakmesb.lelabovert.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hakmesb.lelabovert.exception.ResourceNotFoundException;
import com.hakmesb.lelabovert.model.Account;
import com.hakmesb.lelabovert.model.Address;
import com.hakmesb.lelabovert.model.AlgeriaCity;
import com.hakmesb.lelabovert.model.Customer;
import com.hakmesb.lelabovert.payload.AddOrUpdateCustomerRequest;
import com.hakmesb.lelabovert.payload.AddOrUpdateCustomerResponse;
import com.hakmesb.lelabovert.payload.CustomerDto;
import com.hakmesb.lelabovert.payload.mapper.CustomerDtoMapper;
import com.hakmesb.lelabovert.repository.AccountRepository;
import com.hakmesb.lelabovert.repository.AlgeriaCitiesRepository;
import com.hakmesb.lelabovert.repository.CustomerRepository;

@Transactional
@Service
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final CustomerDtoMapper customerDtoMapper;
	private final AccountRepository accountRepository;
	private final AlgeriaCitiesRepository algeriaCitiesRepository;

	public CustomerService(CustomerRepository customerRepository, CustomerDtoMapper customerDtoMapper,
			AccountRepository accountRepository, AlgeriaCitiesRepository algeriaCitiesRepository) {
		this.customerRepository = customerRepository;
		this.customerDtoMapper = customerDtoMapper;
		this.accountRepository = accountRepository;
		this.algeriaCitiesRepository = algeriaCitiesRepository;
	}

	public AddOrUpdateCustomerResponse addOrUpdateCustomer(AddOrUpdateCustomerRequest request, Account account) {
		Integer algeriaCityId = request.commune();
		AlgeriaCity algeriaCity = algeriaCitiesRepository.findById(algeriaCityId)
				.orElseThrow(() -> new ResourceNotFoundException("Algeria City", "algeriaCityId", algeriaCityId));

		Address address = new Address();
		address.setAlgeriaCity(algeriaCity);
		address.setHomeAddress(request.homeAddress());

		Customer customer = new Customer();
		customer.setAddress(address);
		customer.setEmail(request.email());
		customer.setFirstName(request.firstName());
		customer.setLastName(request.lastName());
		customer.setPhoneNumber(request.phoneNumber());

		if (account != null) {

			if (account.getCustomer() != null) {
				CustomerDto customerDto = updateCustomer(customer, account.getCustomer().getId());
				return new AddOrUpdateCustomerResponse(customerDto, false);
			}

			Customer savedAccountCustomer = customerRepository.save(customer);

			account.setCustomer(savedAccountCustomer);
			accountRepository.save(account);

			CustomerDto customerDto = customerDtoMapper.apply(savedAccountCustomer);

			return new AddOrUpdateCustomerResponse(customerDto, true);
		}

		Customer savedCustomer = customerRepository.save(customer);

		CustomerDto customerDto = customerDtoMapper.apply(savedCustomer);

		return new AddOrUpdateCustomerResponse(customerDto, true);
	}

	private CustomerDto updateCustomer(Customer customer, Integer customerId) {
		Customer dbCustomer = customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", customerId));

		customer.setId(dbCustomer.getId());

		Customer savedCustomer = customerRepository.save(customer);

		return customerDtoMapper.apply(savedCustomer);
	}

}
