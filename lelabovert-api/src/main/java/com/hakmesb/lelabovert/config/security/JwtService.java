package com.hakmesb.lelabovert.config.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("${lelabovert.security.jwt.secret-key}")
	private String secretKey;
	
	@Value("${lelabovert.security.jwt.access-token-expiration}")
	private long accessTokenExpiration;
	
	@Value("${lelabovert.security.jwt.refresh-token-expiration}")
	private long refreshTokenExpiration;
	
	private final TokenRepository tokenRepository;
	
	public JwtService(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}
	
	public String generateAccessToken(Account account) {
		return generateToken(account, accessTokenExpiration);
	}
	
	public String generateRefreshToken(Account account) {
		return generateToken(account, refreshTokenExpiration);
	}
	
	public String generateToken(Account account, long expirationTime) {
		String token = Jwts
				.builder()
				.subject(account.getEmail())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(getSigningKey())
				.compact();
		
		return token;
	}
	
	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
		
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
	
	public boolean isAccessTokenValid(String accessToken, UserDetails account) {
		String email = extractEmail(accessToken);
		boolean isValidAccessToken = tokenRepository.findByAccessToken(accessToken)
				.map(t -> !t.isLoggedOut()).orElse(false);
		
		return (email.equals(account.getUsername())) && !isTokenExpired(accessToken) && isValidAccessToken;
	}
	
	public boolean isRefreshTokenValid(String token, Account account) {
		String email = extractEmail(token);
		boolean isValidRefreshToken = tokenRepository.findByRefreshToken(token)
				.map(t -> !t.isLoggedOut()).orElse(false);
		
		return (email.equals(account.getUsername())) && !isTokenExpired(token) && isValidRefreshToken;
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
}
