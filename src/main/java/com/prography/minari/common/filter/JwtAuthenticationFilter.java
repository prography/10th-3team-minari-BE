package com.prography.minari.common.filter;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.CustomAuthenticationException;
import com.prography.minari.common.handler.CustomAuthenticationEntryPoint;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.prography.minari.common.execption.ErrorCode.*;
import static com.prography.minari.common.util.JwtUtil.ACCESS_TOKEN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthenticationFilter 인입! — URI: {}", request.getRequestURI());

        // Header에서 JWT 추출
        String accessToken = request.getHeader(AUTHORIZATION);

        // 헤더에 Authorization이 존재하지 않을 경우 예외 처리
        if(StringUtils.isBlank(accessToken)) {
            log.info("{} : {}", ACCESS_TOKEN, JWT_NOT_FOUND_EXCEPTION.getMessage());
            customAuthenticationEntryPoint.commence(request, response, new CustomAuthenticationException(JWT_NOT_FOUND_EXCEPTION));
            return;
        }

        // JWT 검증
        try {
            jwtUtil.isValidateToken(accessToken);

            User user = userRepository.findById(Long.parseLong(jwtUtil.getUserId(accessToken)))
                    .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    List.of(new SimpleGrantedAuthority(jwtUtil.getUserRole(accessToken)))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (CustomAuthenticationException e) {
            customAuthenticationEntryPoint.commence(request, response, e);
        }

    }

}
