package com.prography.minari.common.filter;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.common.util.ResponseUtil;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.prography.minari.common.execption.ErrorCode.*;
import static com.prography.minari.common.util.JwtUtil.ACCESS_TOKEN;
import static com.prography.minari.user.enums.UserRole.USER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class AdminJwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AdminJwtAuthenticationFilter(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("AdminJwtAuthenticationFilter 인입! — URI: {}", request.getRequestURI());

        // Header에서 JWT 추출
        String accessToken = request.getHeader(AUTHORIZATION);

        // 헤더에 Authorization이 존재하지 않을 경우 예외 처리
        if(StringUtils.isBlank(accessToken)) {
            log.info("{} : {}", ACCESS_TOKEN, JWT_NOT_FOUND_EXCEPTION.getMessage());
            ResponseUtil.writeUnauthorizedResponse(response, JWT_NOT_FOUND_EXCEPTION.getCode(), JWT_NOT_FOUND_EXCEPTION.getMessage());
            return;
        }

        try {
            jwtUtil.isValidateToken(accessToken);

            User user = userRepository.findById(Long.parseLong(jwtUtil.getUserId(accessToken)))
                    .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority(USER.getRoleName())));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (ApiException e) {
            ResponseUtil.writeUnauthorizedResponse(response, e.getErrorCode(), e.getErrorMessage());
            return;
        }

    }

}
