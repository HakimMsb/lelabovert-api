package com.hakmesb.lelabovert.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hakmesb.lelabovert.model.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {

	@Query("""
			select t from Token t inner join Account a on t.account.id = a.id
			where t.account.id = :accountId and t.loggedOut = false
			""")
	List<Token> findAllTokensByAccountId(Integer accountId);

	Optional<Token> findByAccessToken(String accessToken);

	Optional<Token> findByRefreshToken(String refreshToken);
}
