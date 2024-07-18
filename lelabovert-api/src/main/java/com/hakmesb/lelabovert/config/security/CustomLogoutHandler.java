package com.hakmesb.lelabovert.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.hakmesb.lelabovert.exception.ApiException;
import com.hakmesb.lelabovert.model.Token;
import com.hakmesb.lelabovert.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class CustomLogoutHandler implements LogoutHandler{
	
	private final TokenRepository tokenRepository;
	
	public CustomLogoutHandler(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new ApiException("Already not logged in.");
		}

		String token = authHeader.substring(7);
		
		Token storedToken = tokenRepository.findByToken(token).orElse(null);
		
		if (storedToken.isLoggedOut()) {
			throw new ApiException("Already not logged in.");
		}
		
		if(storedToken != null) {
			storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
		}
	}

}
