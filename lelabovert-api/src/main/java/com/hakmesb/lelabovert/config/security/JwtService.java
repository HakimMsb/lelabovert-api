package com.hakmesb.lelabovert.config.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.hakmesb.lelabovert.model.Account;
import com.hakmesb.lelabovert.repository.TokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final String SECRET_KEY = "551ed7a82c17c347c1869a8c68b9511f9a7726913207aacec7af1bd700b6a249";
	private final TokenRepository tokenRepository;
	
	public JwtService(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}
	
	public String generateToken(Account account) {
		String token = Jwts
				.builder()
				.subject(account.getEmail())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
				.signWith(getSigningKey())
				.compact();
		
		return token;
	}
	
	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	private Claims extractAllClaims(String token) {
		Claims claims = Jwts
				.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		
		return claims;
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> resolver) {
		Claims claims = extractAllClaims(token);
		return resolver.apply(claims);
	}
	
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public boolean isValid(String token, UserDetails account) {
		String email = extractEmail(token);
		boolean isValidToken = tokenRepository.findByToken(token).map(t -> !t.isLoggedOut()).orElse(false);
		
		return (email.equals(account.getUsername())) && !isTokenExpired(token) && isValidToken;
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
}
