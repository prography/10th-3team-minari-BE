package com.prography.minari.common.filter;

import ch.qos.logback.core.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.prography.minari.common.execption.ErrorCode.JWT_NOT_FOUND_EXCEPTION;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserReader userReader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 로그인 URI는 Filter 처리 생략!
        if(request.getRequestURI().startsWith("/api/v1/users/oauth/")){
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 헤더에 Authorization이 존재하지 않을 경우 예외 처리
        if(StringUtils.isBlank(accessToken)) {
            log.info("헤더에 Authorization이 존재하지 않습니다. accessToken: {}", accessToken);
            writeUnauthorizedResponse(response, JWT_NOT_FOUND_EXCEPTION.getCode(), JWT_NOT_FOUND_EXCEPTION.getMessage());
            return;
        }

        // JWT 검증
        try {
            jwtUtil.isValidateToken(accessToken);
        } catch (ApiException e) {
            writeUnauthorizedResponse(response, e.getErrorCode(), e.getErrorMessage());
            return;
        }

        // Authentication Context 등록
        try {
            Long userId = Long.parseLong(jwtUtil.getUserId(accessToken));
            User user = userReader.read(userId); // User 조회
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ApiException e) {
            log.info("{}: {}", e.getErrorCode(), e.getErrorMessage());
            writeUnauthorizedResponse(response, e.getErrorCode(), e.getErrorMessage());
        }

    }

    // Unauthorized Response 공통 예외 처리 Response
    private void writeUnauthorizedResponse(HttpServletResponse response, String errorCode, String errorMessage) {
        try {
            response.setStatus(SC_UNAUTHORIZED);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                    CommonResponse.fail(errorCode, errorMessage)
            ));
        } catch (IOException e) {
            log.info("writeUnauthorizedResponse IOException 발생!");
            throw new RuntimeException(e);
        }
    }

}
