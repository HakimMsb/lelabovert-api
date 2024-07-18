package com.hakmesb.lelabovert.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hakmesb.lelabovert.exception.ApiException;
import com.hakmesb.lelabovert.exception.ResourceNotFoundException;
import com.hakmesb.lelabovert.model.Account;
import com.hakmesb.lelabovert.payload.AccountDto;
import com.hakmesb.lelabovert.payload.AccountResponse;
import com.hakmesb.lelabovert.payload.mapper.AccountDtoMapper;
import com.hakmesb.lelabovert.repository.AccountRepository;

@Transactional
@Service
public class AccountDetailsService implements UserDetailsService{
	
	private final AccountRepository accountRepository;
	private final AccountDtoMapper accountDtoMapper;
	private final PasswordEncoder passwordEncoder;
	
	public AccountDetailsService(AccountRepository accountRepository, AccountDtoMapper accountDtoMapper,
			PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.accountDtoMapper = accountDtoMapper;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User with email'" + email + "' not found"));
		
		return account;
	}
	
	public AccountResponse getAllAccounts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		
		Page<Account> accountsPage = accountRepository.findAll(pageDetails);
		
		List<Account> accounts = accountsPage.getContent();
		
		if (accounts.size() == 0) {
			throw new ApiException("No account exists.");
		}
		
		List<AccountDto> accountDtos = accounts.stream().map(accountDtoMapper).collect(Collectors.toList());
		
		return new AccountResponse(accountDtos, accountsPage.getNumber(), accountsPage.getSize(), accountsPage.getTotalElements(),
				accountsPage.getTotalPages(), accountsPage.isLast());
	}
	
	public AccountDto getAccountById(Integer accountId) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new ResourceNotFoundException("Account", "accountId", accountId));
		
		AccountDto accountDto = accountDtoMapper.apply(account);
		
		return accountDto;
	}
	
	public AccountDto changePassword(Integer accountId, String oldPassword, String newPassword) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new ResourceNotFoundException("Account", "accountId", accountId));
		
		if(!passwordEncoder.matches(oldPassword, account.getPassword())) {
			throw new ApiException("The password you entered does not match our records. Please try again.");
		}
		
		account.setPassword(passwordEncoder.encode(newPassword));
		Account savedAccount = accountRepository.save(account);
		
		return accountDtoMapper.apply(savedAccount);
	}

	public String deleteAccount(Integer accountId) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new ResourceNotFoundException("Account", "accountId", accountId));
		
		accountRepository.delete(account);
		
		return "Account with accountId: " + accountId + " deleted successfully.";
	}
	
}
