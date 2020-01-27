package com.gymbook.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * */
@Configuration
@EnableWebSecurity
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private Environment env;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authProvider());
	}

	@Bean
	public DaoAuthenticationProvider authProvider()
	{
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);

		return authProvider;
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception
	{
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception
	{
		
		http.authorizeRequests().antMatchers("/oauth/token/revokeById/**").permitAll();
		http.authorizeRequests().antMatchers("/secure/two_factor_authentication/verification/**").permitAll();
		
		////////////////////////////////////////////////////////////////////////////////////////
		// to reach H2 db (can't be in prod env)
//		http.authorizeRequests().antMatchers("/db", "/db/**").permitAll();
//		http.csrf().disable();
//		http.headers().frameOptions().disable();
//		http.headers().disable();
		////////////////////////////////////////////////////////////////////////////////////////

		// security headers
		http.headers().cacheControl();
		http.headers().contentTypeOptions();
		http.headers().httpStrictTransportSecurity();
		http.headers().frameOptions();
		http.headers().xssProtection();
		http.headers().contentSecurityPolicy("script-src 'self'");
		http.csrf().ignoringAntMatchers("/oauth/token/revokeById/**");
		http.csrf().ignoringAntMatchers("/secure/two_factor_authentication/verification/**");

		// HTTPS
		http.requiresChannel().anyRequest().requiresSecure();

		// CSRF
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

		//
		http.authorizeRequests().anyRequest().authenticated();

	}

// // //  \\ \\ \\ 

	@Bean(name = "springDataSource")
	@ConfigurationProperties("spring.datasource")
	public DataSource springDataSource()
	{
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));

		return dataSource;
	}

	@Bean(name = "springDSJdbcTemplate")
	public JdbcTemplate springJdbcTemplate(@Qualifier("springDataSource") DataSource springDs)
	{
		return new JdbcTemplate(springDs);
	}

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("springDataSource") DataSource springDs)
	{
		return new NamedParameterJdbcTemplate(springDs);
	}

}
