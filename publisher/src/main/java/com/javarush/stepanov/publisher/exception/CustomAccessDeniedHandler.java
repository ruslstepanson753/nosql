package com.javarush.stepanov.publisher.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

// AccessDeniedHandler
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final JsonErrorResponseHelper errorHelper;

    public CustomAccessDeniedHandler(JsonErrorResponseHelper errorHelper) {
        this.errorHelper = errorHelper;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, IOException {

        errorHelper.writeErrorResponse(
                response,
                request,
                HttpServletResponse.SC_FORBIDDEN,
                "Forbidden",
                "Access denied: " + accessDeniedException.getMessage()
        );
    }
}
