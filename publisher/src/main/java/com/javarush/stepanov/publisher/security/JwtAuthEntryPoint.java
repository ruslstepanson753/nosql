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

        // Проверяем, не была ли уже установлена ошибка с другим статус-кодом
        if (response.getStatus() != HttpServletResponse.SC_OK &&
                response.getStatus() != HttpServletResponse.SC_UNAUTHORIZED) {

            // Если статус уже установлен и это не 200 или 401, сохраняем его
            String errorName = getErrorNameByStatus(response.getStatus());
            errorHelper.writeErrorResponse(
                    response,
                    request,
                    response.getStatus(),
                    errorName,
                    "Error: " + authException.getMessage()
            );
            return;
        }

        // Стандартная обработка 401
        errorHelper.writeErrorResponse(
                response,
                request,
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                "Authentication failed: " + authException.getMessage()
        );
    }

    private String getErrorNameByStatus(int statusCode) {
        return switch (statusCode) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Error";
        };
    }
}