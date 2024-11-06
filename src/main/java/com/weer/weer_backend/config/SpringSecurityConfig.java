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
                .authorizeRequests()
                .requestMatchers("/auth/**","/main").permitAll() // 인증 없이 접근 허용
                .anyRequest().authenticated(); // 그 외의 요청은 인증 필요
        return http.build();
    }
}
