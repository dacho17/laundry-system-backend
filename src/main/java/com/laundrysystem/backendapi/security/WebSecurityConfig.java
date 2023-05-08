package com.laundrysystem.backendapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.laundrysystem.backendapi.services.AuthApiService;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
	
	@Autowired
	private AuthApiService authService;

	@Autowired
	private ApiAuthEntryPoint unauthorizedHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ApiAuthTokenFilter authenticationJwtTokenFilter() {
		return new ApiAuthTokenFilter();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(authService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
		return authConfiguration.getAuthenticationManager();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new ApiAccessDeniedHandler();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
		.exceptionHandling()
			.authenticationEntryPoint(unauthorizedHandler)
			.accessDeniedHandler(accessDeniedHandler())	// NOTE: Try to autowire this
		.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().authorizeHttpRequests()
			.requestMatchers(HttpMethod.GET, "/auth/**").permitAll()
			.requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
			.requestMatchers(HttpMethod.GET, "/account/**").hasRole("TENANT")
			.requestMatchers(HttpMethod.POST, "/account/**").hasRole("TENANT")
			.requestMatchers(HttpMethod.GET, "/availability/**").hasRole("TENANT")
			.requestMatchers(HttpMethod.POST, "/availability/**").hasRole("TENANT")
			.requestMatchers(HttpMethod.GET, "/booking/**").hasRole("TENANT")
			.requestMatchers(HttpMethod.POST, "/booking/**").hasRole("TENANT")
			.requestMatchers(HttpMethod.GET, "/residence-admin/**").hasRole("RESIDENCE_ADMIN") // 
			.requestMatchers(HttpMethod.POST, "/residence-admin/**").hasRole("RESIDENCE_ADMIN") // 


//			.antMatchers(HttpMethod.GET, "/docs/**").permitAll()
//			.antMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
//			.antMatchers(HttpMethod.POST, "/swagger-ui/**").permitAll()
//			.antMatchers(HttpMethod.DELETE, "/swagger-ui/**").permitAll()
//			.antMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
			.anyRequest().authenticated();

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
