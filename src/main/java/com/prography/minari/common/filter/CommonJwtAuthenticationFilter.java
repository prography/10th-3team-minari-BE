package com.prography.minari.common.filter;

import com.prography.minari.user.entity.User;
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

import static com.prography.minari.social.dto.enums.SocialType.KAKAO;

@Slf4j
public class CommonJwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("CommonJwtAuthenticationFilter 인입! — URI: {}", request.getRequestURI());
        log.info("request.getRequestURI().startsWith(\"/api/v1/\"): {}", request.getRequestURI().startsWith("/api/v1/"));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(User.create("email", KAKAO, 1L, "name", "image", "uuid"), null, List.of(new SimpleGrantedAuthority("ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
