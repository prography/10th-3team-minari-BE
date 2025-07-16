package com.prography.minari.common.config;

import com.prography.minari.common.filter.AdminJwtAuthenticationFilter;
import com.prography.minari.common.filter.CommonJwtAuthenticationFilter;
import com.prography.minari.common.filter.JwtAuthenticationFilter;

import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AdminJwtAuthenticationFilter adminJwtAuthenticationFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtUtil jwtUtil, UserRepository userRepository) {
        this.adminJwtAuthenticationFilter = new AdminJwtAuthenticationFilter(userRepository, jwtUtil);
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, userRepository);
    }

    /** 1. Swagger & 공개용 필터 */
    @Bean
    @Order(1)
    public SecurityFilterChain publicFilter(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(request -> {
                    String uri = request.getRequestURI();
                    return uri.startsWith("/swagger-ui") ||
                            uri.startsWith("/v3/api-docs") ||
                            uri.startsWith("/swagger-resources") ||
                            uri.startsWith("/webjars") ||
                            uri.startsWith("/favicon.ico") ||
                            uri.startsWith("/api/v1/users/oauth") ||
                            uri.startsWith("/api/v1/users/token/refresh") ||
                            uri.startsWith("/api/v1/dev");
                })
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }

    /** 2. Admin API 전용 필터 */
    @Bean
    @Order(2)
    public SecurityFilterChain adminFilter(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(request -> request.getRequestURI().startsWith("/admin/"))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(adminJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /** 3. 일반 API 필터 */
    @Bean
    @Order(3)
    public SecurityFilterChain apiFilter(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(request -> request.getRequestURI().startsWith("/api/v1/"))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

