package com.hakmesb.lelabovert.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hakmesb.lelabovert.config.AppConstants;
import com.hakmesb.lelabovert.filter.JwtAuthenticationFilter;
import com.hakmesb.lelabovert.service.AccountDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AccountDetailsService accountService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomLogoutHandler customLogoutHandler;

	public SecurityConfig(AccountDetailsService accountService, JwtAuthenticationFilter authFilter,
			CustomLogoutHandler customLogoutHandler) {
		this.accountService = accountService;
		this.jwtAuthenticationFilter = authFilter;
		this.customLogoutHandler = customLogoutHandler;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(req -> req
						.requestMatchers(AppConstants.PUBLIC_URLS).permitAll()
						.requestMatchers("/error/**").permitAll()
						.requestMatchers(AppConstants.USER_URLS).hasAnyAuthority("USER", "ADMIN")
						.requestMatchers(AppConstants.ADMIN_URLS).hasAuthority("ADMIN") 
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll())
				.userDetailsService(accountService)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(e -> e
						.accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(403))
						.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.logout(l -> l.logoutUrl("/api/v1/logout").addLogoutHandler(customLogoutHandler).logoutSuccessHandler(
						(request, response, authentication) -> SecurityContextHolder.clearContext()))
				.build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

}
