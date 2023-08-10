package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "resource_id";
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
//                anonymous().disable()
				.authorizeRequests()
//--------------students------------------------------------------------------------------------------------------------
				.antMatchers(HttpMethod.GET, "/v1/students/get-student/{email}")
				.access("hasAnyRole('ROLE_ADMIN')")
				.antMatchers(HttpMethod.POST, "/v1/students/add")
				.access("hasAnyRole('ROLE_ADMIN')")
				.and()
				.exceptionHandling()
				.accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}

}