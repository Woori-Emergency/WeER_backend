package com.weer.weer_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 보호 비활성화 (개발용)
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/api/emergency").permitAll() // /api/emergency 엔드포인트 접근 허용
                        .anyRequest().permitAll() // 그 외의 요청도 모두 접근 허용
                );

        return http.build();
    }
}
