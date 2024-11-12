package com.weer.weer_backend.filter;

import com.weer.weer_backend.dto.SecurityUser;
import com.weer.weer_backend.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTFilter extends OncePerRequestFilter {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(JWTFilter.class);
    private final JWTUtil jwtUtil;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("doFILTERINTERNAL START");
        String requestURI = request.getRequestURI();

        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.split(" ")[1];
            if (this.jwtUtil.isExpired(token)) {
                System.out.println("token expired");
                filterChain.doFilter(request, response);
                log.info("doFILTER_EXPIRED");
            } else {
                String loginId = this.jwtUtil.getLoginId(token);
                User user = User.builder()
                        .loginId(loginId)
                        // 임시비밀번호 설정.
                        .password("tempPW")
                        .build();

                SecurityUser securityUser = new SecurityUser(user);
                Authentication authToken = new UsernamePasswordAuthenticationToken(securityUser, (Object)null, securityUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
                log.info("doFILTER_FINISH");
            }
        } else {
            filterChain.doFilter(request, response);
            log.info("doFILTER_NULL");
        }
    }

    @Generated
    public JWTFilter(final JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
}

