package com.prography.minari.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import com.prography.minari.user.service.impl.UserReader;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.prography.minari.common.execption.ErrorCode.*;
import static com.prography.minari.common.util.JwtUtil.ACCESS_TOKEN;
import static com.prography.minari.common.util.JwtUtil.REFRESH_TOKEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private static final List<String> PERMIT_ALL_PATHS = Arrays.asList(
            "/api/v1/users/oauth",
            "/api/v1/users/token/refresh",
            "/swagger-ui",
            "/v3/api-docs",
            "/swagger-resources",
            "/webjars",
            "/favicon.ico"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 리스트에 포함된 경로는 필터를 그냥 통과시킴
        if (PERMIT_ALL_PATHS.stream().anyMatch(request.getRequestURI()::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에 accessToken이 없으면 쿠키에서 accessToken을 찾음
        // String accessToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
        //        .filter(cookie -> ACCESS_TOKEN.equals(cookie.getName()))
        //        .map(Cookie::getValue)
        //        .findFirst()
        //        .orElse(null);

        String accessToken = request.getHeader(AUTHORIZATION);

        log.info("추출한 accessToken : {}", accessToken);

        // 헤더에 Authorization이 존재하지 않을 경우 예외 처리
        if(StringUtils.isBlank(accessToken)) {
            log.info("{} : {}", ACCESS_TOKEN, JWT_NOT_FOUND_EXCEPTION.getMessage());
            writeUnauthorizedResponse(response, JWT_NOT_FOUND_EXCEPTION.getCode(), JWT_NOT_FOUND_EXCEPTION.getMessage());
            return;
        }

        // JWT 검증
        try {
            jwtUtil.isValidateToken(accessToken);

            User user = userRepository.findById(Long.parseLong(jwtUtil.getUserId(accessToken)))
                    .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch(ExpiredJwtException e) {
            log.info("{}, {}", ACCESS_TOKEN, JWT_EXPIRED_EXCEPTION.getMessage());

            // refreshToken 쿠키에서 꺼내기
            String refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                    .filter(cookie -> REFRESH_TOKEN.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);

            log.info("추출한 refreshToken : {}", refreshToken);

            // refreshToken이 없으면 401
            if (StringUtils.isBlank(refreshToken)) {
                log.info("{}, {}", REFRESH_TOKEN, JWT_NOT_FOUND_EXCEPTION.getMessage());
                writeUnauthorizedResponse(response, JWT_NOT_FOUND_EXCEPTION.getCode(), JWT_NOT_FOUND_EXCEPTION.getMessage());
                return;
            }

            // refreshToken 유효성 검증
            try {
                jwtUtil.isValidateToken(refreshToken);
                String userId = jwtUtil.getUserId(refreshToken);

                // JWT 생성
                String newAccessToken = jwtUtil.createAccessToken(userId);
                String newRefreshToken = jwtUtil.createRefreshToken(userId);

                // Cookie 생성
                ResponseCookie newAccessTokenCookie = jwtUtil.createAccessTokenCookie(newAccessToken);
                ResponseCookie newRefreshTokenCookie = jwtUtil.createRefreshTokenCookie(newRefreshToken);

                response.setHeader(SET_COOKIE, newAccessTokenCookie.toString());
                response.setHeader(SET_COOKIE, newRefreshTokenCookie.toString());

                // SecurityContext에 인증 정보 등록
                User user = userRepository.findById(Long.parseLong(userId))
                        .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("TOKEN REFRESH SUCCESS : {}", newAccessToken);

                // 다음 필터로 진행
                filterChain.doFilter(request, response);
            } catch (ApiException ex) {
                // refreshToken도 만료/유효하지 않으면 401
                writeUnauthorizedResponse(response, ex.getErrorCode(), ex.getErrorMessage());
            } catch (ExpiredJwtException ex) {
                writeUnauthorizedResponse(response, JWT_EXPIRED_EXCEPTION.getCode(), JWT_EXPIRED_EXCEPTION.getMessage());
            }
        } catch (ApiException e) {
            writeUnauthorizedResponse(response, e.getErrorCode(), e.getErrorMessage());
        }
    }

    // Unauthorized Response 공통 예외 처리 Response
    private void writeUnauthorizedResponse(HttpServletResponse response, String errorCode, String errorMessage) {
        log.info("{} : {}", errorCode, errorMessage);
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
