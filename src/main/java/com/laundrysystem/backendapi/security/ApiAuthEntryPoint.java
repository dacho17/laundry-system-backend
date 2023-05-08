package com.laundrysystem.backendapi.security;

import java.io.IOException;
import java.io.PrintWriter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.laundrysystem.backendapi.dtos.ResponseObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiAuthEntryPoint implements AuthenticationEntryPoint {
	
	private static final String unauthenticatedUser = "The user failed to authenticate.";
	
	private static final Logger logger = LoggerFactory.getLogger(ApiAuthEntryPoint.class);
	
	private String transformResToJsonString(String excStr, Exception exc) {
		Gson gson = new GsonBuilder().create();
		
		return gson.toJson(new ResponseObject<String>(excStr, exc.getMessage()));
	}
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.warn(String.format("Client attempted to access a resource while not being authenticated: " + authException.getMessage()));
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(transformResToJsonString(unauthenticatedUser, authException));
        out.flush();
 	}
}
