package com.weer.weer_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weer.weer_backend.filter.JWTFilter;
import com.weer.weer_backend.filter.LoginFilter;
import com.weer.weer_backend.filter.TokenProvider;  // Updated to use TokenProvider
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
  private final AuthenticationConfiguration authenticationConfiguration;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final TokenProvider tokenProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/**", "/auth/**", "swagger-ui/**", "/v3/api-docs/**").permitAll()
            .anyRequest().authenticated())
        .exceptionHandling(handling -> handling
            .authenticationEntryPoint((request, response, authException) -> {
              response.setStatus(HttpStatus.UNAUTHORIZED.value());
              response.setContentType(MediaType.APPLICATION_JSON_VALUE);

              Map<String, Object> errorDetails = new HashMap<>();
              errorDetails.put("message", "인증에 실패했습니다");
              errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());

              response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
            }));
    http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), tokenProvider),
        UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(new JWTFilter(tokenProvider), LoginFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // PasswordEncoder Bean 등록
  }
}
