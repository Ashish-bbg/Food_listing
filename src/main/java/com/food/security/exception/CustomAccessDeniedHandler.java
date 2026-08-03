package com.food.security.exception;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.dto.response.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		 ErrorResponse errorResponse = ErrorResponse.builder()
			.timeStamp(LocalDateTime.now())
			.status(HttpServletResponse.SC_FORBIDDEN)
			.error("Forbidden")
			.message(accessDeniedException.getMessage())
			.path(request.getRequestURI())
			.build();
		
		objectMapper.writeValue(response.getWriter(), errorResponse);
		
	}

}
