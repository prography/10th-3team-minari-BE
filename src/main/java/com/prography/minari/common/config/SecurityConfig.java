package com.prography.minari.common.config;

import com.prography.minari.common.filter.JwtAuthenticationFilter;

import com.prography.minari.common.handler.CustomAccessDeniedHandler;
import com.prography.minari.common.handler.CustomAuthenticationEntryPoint;
import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.user.repository.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.prography.minari.user.enums.UserRole.ADMIN;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(JwtUtil jwtUtil,
                          UserRepository userRepository,
                          CustomAccessDeniedHandler customAccessDeniedHandler,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, userRepository, customAuthenticationEntryPoint);
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
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
                            uri.startsWith("/api/v1/toss");
                })
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();
    }

    /** 2. JwtAuthentication 필터 **/
    @Bean
    @Order(2)
    public SecurityFilterChain apiFilter(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(request -> request.getRequestURI().startsWith("/api/v1/"))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 401
                        .accessDeniedHandler(customAccessDeniedHandler)           // 403
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/admin/**").hasAuthority(ADMIN.getRoleName())
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /** 3. CORS 설정 */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "https://minari-official.com",
                "https://minari-staging.netlify.app",
                "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // ✅ 크로스도메인 쿠키 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

