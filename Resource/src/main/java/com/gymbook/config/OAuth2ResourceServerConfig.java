package com.gymbook.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter
{
	@Autowired
	private Environment env;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(final HttpSecurity http) throws Exception
	{
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

		// Enable CSRF Protection
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

		///////////////////////////////////////////////////////////////////////////////////////////////
		// for H2 reach (can't be in prod env)
//		http.authorizeRequests().antMatchers("/db", "/db/**").permitAll();
//		http.csrf().disable();
//		http.headers().frameOptions().disable();
//		http.headers().disable();
		///////////////////////////////////////////////////////////////////////////////////////////////

		// security headers
		http.headers().cacheControl();
		http.headers().contentTypeOptions();
		http.headers().httpStrictTransportSecurity();
		http.headers().frameOptions();//.sameOrigin();
		http.headers().xssProtection();
		http.headers().contentSecurityPolicy("script-src 'self'");

		// securing update password request
		http.authorizeRequests().antMatchers("/user/changePassword*").hasAuthority("CHANGE_PASSWORD_PRIVILEGE");

		//
		http.authorizeRequests().anyRequest().authenticated();
		
		// HTTPS
		http.requiresChannel().anyRequest().requiresSecure();

		// 401 UNAUTHORIZED instead of 403
		http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		
	}

	@Primary
	@Bean
	public RemoteTokenServices tokenService()
	{
		RemoteTokenServices tokenService = new RemoteTokenServices();
		tokenService.setCheckTokenEndpointUrl(env.getProperty("url.checkTokenEndpoint"));
		tokenService.setClientId(env.getProperty("oauth.clientId"));
		tokenService.setClientSecret(env.getProperty("oauth.clientSecret"));
		return tokenService;
	}
}
