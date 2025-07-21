package com.prography.minari.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.minari.common.execption.CustomAuthenticationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.prography.minari.common.execption.ErrorCode.JWT_NOT_ADMIN;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        String code    = "JWT000";
        String message = "인증에 실패하였습니다.";

        if (authException instanceof CustomAuthenticationException ex) {
            code = ex.getErrorCode().getCode();
            message = ex.getErrorCode().getMessage();
        }

        objectMapper.writeValue(response.getWriter(), Map.of(
                "code", code,
                "message", message
        ));

    }
}
