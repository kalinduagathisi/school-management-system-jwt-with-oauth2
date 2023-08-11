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

	// Define a resource identifier
	private static final String RESOURCE_ID = "resource_id";

	// Configure the resource server's security settings
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}

	// Configure the HTTP security settings for the resource server
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				// Define URL patterns and access rules based on roles

				.antMatchers(HttpMethod.GET, "/v1/users/user/{email}")
				.access("hasRole('ROLE_ADMIN')")

				.antMatchers(HttpMethod.GET, "/v1/users/get-all-users")
				.access("hasRole('ROLE_ADMIN')")

				.antMatchers(HttpMethod.PUT, "/v1/users/get-all-users")
				.access("hasRole('ROLE_ADMIN')")



				.antMatchers(HttpMethod.GET, "/v1/students/get-student/{email}")
				.access("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")

				.antMatchers(HttpMethod.GET, "/v1/students/get-all-students")
				.access("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")

				.antMatchers(HttpMethod.GET, "/v1/students/get-students/filter")
				.access("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")

				.antMatchers(HttpMethod.GET, "/v1/students/get-students/filter/range")
				.access("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")

				.antMatchers(HttpMethod.POST, "/v1/students/add")
				.access("hasAnyRole('ROLE_ADMIN')")

				.antMatchers(HttpMethod.PUT, "/v1/students/update")
				.access("hasAnyRole('ROLE_ADMIN')")




				.antMatchers(HttpMethod.POST, "/v1/payments/add")
				.access("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")

				.antMatchers(HttpMethod.GET, "/v1/payments/get-scheme/{name}")
				.access("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")

				.antMatchers(HttpMethod.GET, "/v1/payments/get-all-schemes")
				.access("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")

				.and()
				.exceptionHandling()
				.accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}
}
