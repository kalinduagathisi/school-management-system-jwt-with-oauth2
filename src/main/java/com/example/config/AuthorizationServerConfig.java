package com.example.config;

import com.example.constants.OAuth2Constant;
import com.example.exception.CustomOauthException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	// Injecting necessary components
	private final AuthenticationManager authenticationManager;
	private final BCryptPasswordEncoder encoder;
	private final CustomTokenEnhancer customTokenEnhancer;

	public AuthorizationServerConfig(AuthenticationManager authenticationManager,
									 BCryptPasswordEncoder encoder,
									 CustomTokenEnhancer customTokenEnhancer) {
		this.authenticationManager = authenticationManager;
		this.encoder = encoder;
		this.customTokenEnhancer = customTokenEnhancer;
	}

	// Configuring client details for OAuth2 clients
	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		configurer
				.inMemory()
				.withClient(OAuth2Constant.ADMIN_CLIENT_ID)
				.secret(encoder.encode(OAuth2Constant.CLIENT_SECRET))
				.authorizedGrantTypes(OAuth2Constant.GRANT_TYPE_PASSWORD, OAuth2Constant.AUTHORIZATION_CODE, OAuth2Constant.REFRESH_TOKEN, OAuth2Constant.IMPLICIT)
				.scopes(OAuth2Constant.SCOPE_READ, OAuth2Constant.SCOPE_WRITE, OAuth2Constant.TRUST)
				.accessTokenValiditySeconds(OAuth2Constant.ACCESS_TOKEN_VALIDITY_SECONDS)
				.refreshTokenValiditySeconds(OAuth2Constant.REFRESH_TOKEN_VALIDITY_SECONDS);
	}

	// Configuring various endpoints and their behavior for authorization
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

		// Setting up a chain of token enhancers
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));

		// Configuring authorization server endpoints
		endpoints.tokenStore(tokenStore())
				.tokenEnhancer(enhancerChain)
				.authenticationManager(authenticationManager)
				.accessTokenConverter(accessTokenConverter())
				.pathMapping("/oauth/token", "/v1/authorize")  // Mapping the token endpoint to a custom URL
				.exceptionTranslator(exception -> {
					if (exception instanceof InvalidGrantException)
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomOauthException("invalid credentials."));
					else
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomOauthException((exception.getMessage()) != null ?
								exception.getMessage(): "Sorry, something went wrong."));
				});
	}

	// Bean definition for creating a JwtAccessTokenConverter
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(OAuth2Constant.TOKEN_SIGN_IN_KEY);
		return converter;
	}

	// Bean definition for creating a JwtTokenStore
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	// Bean definition for custom TokenEnhancer
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return customTokenEnhancer;
	}
}
