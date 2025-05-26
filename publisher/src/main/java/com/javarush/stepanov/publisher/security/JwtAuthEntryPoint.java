package com.javarush.stepanov.publisher.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.stepanov.publisher.exception.JsonErrorResponseHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

// Обновленный AuthenticationEntryPoint
@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final JsonErrorResponseHelper errorHelper;

    public JwtAuthEntryPoint(JsonErrorResponseHelper errorHelper) {
        this.errorHelper = errorHelper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        errorHelper.writeErrorResponse(
                response,
                request,
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                "Authentication failed: " + authException.getMessage()
        );
    }
}