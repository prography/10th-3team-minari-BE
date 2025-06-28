package com.prography.minari.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    //TODO 운영 배포시, 실제 경로만 허용할 수 있도록 수정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**") // 모든 경로 허용
                .allowedOrigins("*") // 모든 Origin 허용 (프론트 도메인 제한 없음)
                .allowedMethods("*") // GET, POST, PUT, DELETE 등 모든 HTTP 메서드 허용
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(false); // 인증정보 포함 여부

        registry.addMapping("/admin/api/v1/**") // admin 경로 허용
                .allowedOrigins("*") // 모든 Origin 허용 (프론트 도메인 제한 없음)
                .allowedMethods("*") // GET, POST, PUT, DELETE 등 모든 HTTP 메서드 허용
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(false); // 인증정보 포함 여부
    }

}
