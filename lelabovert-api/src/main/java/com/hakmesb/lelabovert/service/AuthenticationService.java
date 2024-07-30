package com.hakmesb.lelabovert.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.config.security.JwtService;
import com.hakmesb.lelabovert.exception.ApiException;
import com.hakmesb.lelabovert.exception.ResourceNotFoundException;
import com.hakmesb.lelabovert.exception.UnauthorizedException;
import com.hakmesb.lelabovert.model.Account;
import com.hakmesb.lelabovert.model.Cart;
import com.hakmesb.lelabovert.model.Customer;
import com.hakmesb.lelabovert.model.Role;
import com.hakmesb.lelabovert.model.Roles;
import com.hakmesb.lelabovert.model.Token;
import com.hakmesb.lelabovert.payload.AuthenticationResponse;
import com.hakmesb.lelabovert.payload.LoginRequest;
import com.hakmesb.lelabovert.payload.RegistrationRequest;
import com.hakmesb.lelabovert.repository.AccountRepository;
import com.hakmesb.lelabovert.repository.CartRepository;
import com.hakmesb.lelabovert.repository.CustomerRepository;
import com.hakmesb.lelabovert.repository.RoleRepository;
import com.hakmesb.lelabovert.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService {

	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final RoleRepository roleRepository;
	private final AuthenticationManager authenticationManager;
	private final TokenRepository tokenRepository;
	private final CustomerRepository customerRepository;
	private final CartRepository cartRepository;
	
	public AuthenticationService(AccountRepository accountRepository, PasswordEncoder passwordEncoder,
			JwtService jwtService, RoleRepository roleRepository, AuthenticationManager authenticationManager,
			TokenRepository tokenRepository, CustomerRepository customerRepository, CartRepository cartRepository) {
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.roleRepository = roleRepository;
		this.authenticationManager = authenticationManager;
		this.tokenRepository = tokenRepository;
		this.customerRepository = customerRepository;
		this.cartRepository = cartRepository;
	}
	
	public AuthenticationResponse register(RegistrationRequest request) {
		Integer customerId = request.customerId().orElse(null);
		
		Optional<Account> existingAccount = accountRepository.findByEmail(request.email());
		if(existingAccount.isPresent()) {
			throw new ApiException("Account with email: " + request.email() + " already exists.");
		}
		
		Account account = new Account();
		account.setEmail(request.email());
		account.setPassword(passwordEncoder.encode(request.password()));

		if(customerId != null) {
			Customer customer = customerRepository.findById(customerId)
					.orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", customerId));
			account.setCustomer(customer);
		}
		if(request.roles().isEmpty()) {
			List<Role> rolesSet = Arrays.asList(roleRepository.findOneByName(Roles.USER.toString()).get());
			account.setRolesSet(rolesSet);
		}else {
			List<Role> requestRolesSet = request.roles().get();
			List<Role> dbRolesSet = requestRolesSet.stream().map(role -> roleRepository.findOneByName(role.getName())
					.orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", role.getName())))
					.collect(Collectors.toList());
			
			account.setRolesSet(dbRolesSet);	
		}
		
		account = accountRepository.save(account);
		
		Cart cart = new Cart();
		cart.setAccount(account);
		cart.setTotalAmount(0.0f);
		cart = cartRepository.save(cart);
		
		String accessToken = jwtService.generateAccessToken(account);
		String refreshToken = jwtService.generateRefreshToken(account);
		
		saveAccountToken(account, accessToken, refreshToken);
		
		return new AuthenticationResponse(accessToken, refreshToken);
	}
	
	public AuthenticationResponse authenticate(LoginRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.email(),
							request.password()
							)
					);
		} catch (AuthenticationException e) {
			throw new ApiException(e.getMessage());
		}
		
		Account account = accountRepository.findByEmail(request.email())
				.orElseThrow(() -> new UsernameNotFoundException("User with email'" + request.email() + "' not found"));
		
		String accessToken = jwtService.generateAccessToken(account);
		String refreshToken = jwtService.generateRefreshToken(account);
		
		revokeAllTokensByAccount(account);
		
		saveAccountToken(account, accessToken, refreshToken);
		
		return new AuthenticationResponse(accessToken, refreshToken);
	}
	
	private void saveAccountToken(Account account, String accessToken, String refreshToken) {
		Token token = new Token();
		token.setAccessToken(accessToken);
		token.setRefreshToken(refreshToken);
		token.setLoggedOut(false);
		token.setAccount(account);
		tokenRepository.save(token);
	}	
	
	private void revokeAllTokensByAccount(Account account) {
		List<Token> validTokenListByUser = tokenRepository.findAllTokensByAccountId(account.getId());
		
		if(!validTokenListByUser.isEmpty()) {
			validTokenListByUser.forEach(t -> t.setLoggedOut(true));
		}
		
		tokenRepository.saveAll(validTokenListByUser);
	}

	public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new ApiException("Invalid authorization header");
		}
		
		String token = authHeader.substring(7);
		
		String accountEmail = jwtService.extractEmail(token);
		
		Account account = accountRepository.findByEmail(accountEmail)
				.orElseThrow(() -> new UsernameNotFoundException("No account found"));
		
		if(jwtService.isRefreshTokenValid(token, account)) {
			String accessToken = jwtService.generateAccessToken(account);
			String refreshToken = jwtService.generateRefreshToken(account);
			
			revokeAllTokensByAccount(account);
			
			saveAccountToken(account, accessToken, refreshToken);
			
			return new AuthenticationResponse(accessToken, refreshToken);
		}
		
		throw new UnauthorizedException("Invalid refresh token");
	}
	
}
