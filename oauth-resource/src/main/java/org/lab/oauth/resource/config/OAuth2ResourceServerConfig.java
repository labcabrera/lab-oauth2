package org.lab.oauth.resource.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@PropertySource({ "classpath:persistence.properties" })
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Override
	public void configure(final HttpSecurity http) throws Exception {
		http.sessionManagement() //
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) //
				.and() //
				.authorizeRequests().anyRequest().permitAll();
	}

	// Remote token service
	/*
	 * @Primary
	 * 
	 * @Bean public RemoteTokenServices tokenService() { final
	 * RemoteTokenServices tokenService = new RemoteTokenServices();
	 * tokenService.setCheckTokenEndpointUrl(
	 * "http://localhost:8081/spring-security-oauth-server/oauth/check_token");
	 * tokenService.setClientId("fooClientIdPassword");
	 * tokenService.setClientSecret("secret"); return tokenService; }
	 */

	// JWT token store

	@Override
	public void configure(final ResourceServerSecurityConfigurer config) {
		config.tokenServices(tokenServices());
	}

	/*
	 * @Bean public TokenStore tokenStore() { return new
	 * JwtTokenStore(accessTokenConverter()); }
	 * 
	 * @Bean public JwtAccessTokenConverter accessTokenConverter() { final
	 * JwtAccessTokenConverter converter = new JwtAccessTokenConverter(); //
	 * converter.setSigningKey("123"); final Resource resource = new
	 * ClassPathResource("public.txt"); String publicKey = null; try { publicKey
	 * = IOUtils.toString(resource.getInputStream()); } catch (final IOException
	 * e) { throw new RuntimeException(e); }
	 * converter.setVerifierKey(publicKey); return converter; }
	 */
	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		return defaultTokenServices;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}

}
