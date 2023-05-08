package com.laundrysystem.backendapi.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.laundrysystem.backendapi.services.AuthApiService;
import com.laundrysystem.backendapi.utils.JwtUtils;

public class ApiAuthTokenFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiAuthTokenFilter.class);
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private AuthApiService authService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = jwtUtils.parseJwt(request);
		
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				String username = jwtUtils.getUserNameFromJwtToken(jwt);

				UserDetails userDetails = authService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				
				logger.info(String.format("User authentication=[%s].\n\n", authentication.toString()));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
				logger.info(String.format("User with [username=%s] is successfully authenticated.", username));
			} else if (jwt != null) {
				String username = jwtUtils.getUserNameFromJwtToken(jwt);
				logger.info(String.format("Jwt exist, but is invalid. Removing jwt from the user with username=%s", username));
				authService.removeJwtFromUser(jwt, username);
			}
		} catch (Exception exc) {
			logger.error(exc.getStackTrace().toString());
		}
	
		filterChain.doFilter(request, response);
	}
}
