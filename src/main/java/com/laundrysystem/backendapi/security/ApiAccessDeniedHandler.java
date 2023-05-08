package com.laundrysystem.backendapi.security;

import java.io.IOException;
import java.io.PrintWriter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.laundrysystem.backendapi.dtos.ResponseObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ApiAccessDeniedHandler implements AccessDeniedHandler {
	
	private static final String unauthorizedUser = "This user is unauthorized to perform the requested operation.";
	
	private static final Logger logger = LoggerFactory.getLogger(ApiAccessDeniedHandler.class);
	
	private String transformResToJsonString(String excStr, Exception exc) {
		Gson gson = new GsonBuilder().create();
		
		return gson.toJson(new ResponseObject<String>(excStr, exc.getMessage()));
	}
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		logger.warn(String.format("AccessDeniedException occured for the user: " + accessDeniedException.getMessage()));
		
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print(transformResToJsonString(unauthorizedUser, accessDeniedException));
        out.flush();
	}
}
