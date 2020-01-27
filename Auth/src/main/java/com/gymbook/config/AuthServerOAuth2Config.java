package com.gymbook.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import com.gymbook.tfa.CustomOAuth2RequestFactory;
import com.gymbook.tfa.TwoFactorAuthenticationFilter;

@Configuration
@EnableAuthorizationServer
@PropertySource(
{ "classpath:persistence.properties" })
public class AuthServerOAuth2Config extends AuthorizationServerConfigurerAdapter
{
	@Autowired
	private Environment env;

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Value("classpath:schema.sql")
	private Resource schemaScript;

	@Value("classpath:data.sql")
	private Resource dataScript;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception
	{
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception
	{
		clients.jdbc(dataSource());
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
	{
		final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();

		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer()));

		endpoints.tokenStore(tokenStore()).tokenEnhancer(tokenEnhancerChain).authenticationManager(authenticationManager).requestFactory(customOAuth2RequestFactory());
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices()
	{
		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);

		return defaultTokenServices;
	}

	@Bean
	public TokenEnhancer tokenEnhancer()
	{
		return new CustomTokenEnhancer();
	}

// // // JDBC token store configuration \\ \\ \\

	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource)
	{
		DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		initializer.setDatabasePopulator(databasePopulator());
		return initializer;
	}

	private DatabasePopulator databasePopulator()
	{
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(schemaScript);
		populator.addScript(dataScript);
		return populator;
	}

	@Bean
	public DataSource dataSource()
	{
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.user"));
		dataSource.setPassword(env.getProperty("jdbc.pass"));

		return dataSource;
	}

	@Bean
	public TokenStore tokenStore()
	{
		return new JdbcTokenStore(dataSource());
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}

// // // 2FA \\ \\ \\

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Bean
	public DefaultOAuth2RequestFactory customOAuth2RequestFactory()
	{
		return new CustomOAuth2RequestFactory(clientDetailsService);
	}

	@Bean
	public FilterRegistrationBean twoFactorAuthenticationFilterRegistration()
	{
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(twoFactorAuthenticationFilter());
		registration.addUrlPatterns("/oauth/token");
		registration.setName("twoFactorAuthenticationFilter");
		return registration;
	}

	@Bean
	public TwoFactorAuthenticationFilter twoFactorAuthenticationFilter()
	{
		return new TwoFactorAuthenticationFilter();
	}

}
