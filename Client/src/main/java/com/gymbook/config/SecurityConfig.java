package com.gymbook.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Override
	protected void configure(final HttpSecurity http) throws Exception
	{
		http.authorizeRequests().anyRequest().permitAll();

		// security headers
		http.headers().cacheControl();
		http.headers().contentTypeOptions();
		http.headers().httpStrictTransportSecurity();
		http.headers().frameOptions();
		http.headers().xssProtection();
		http.headers().contentSecurityPolicy("script-src 'self' 'unsafe-eval'");

		// HTTPS
		http.requiresChannel().anyRequest().requiresSecure();

		// CSRF
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}
}
