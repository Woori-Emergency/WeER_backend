package com.weer.weer_backend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weer.weer_backend.dto.SecurityUser;
import com.weer.weer_backend.enums.Approve;
import com.weer.weer_backend.exception.CustomException;
import com.weer.weer_backend.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.GrantedAuthority;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private static final String LOGIN_ID_PARAMETER = "loginId"; // 로그인 ID 파라미터 이름
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

        setFilterProcessesUrl("/auth/login-process");

    }
    // 로그인 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.info("ATTEMPT_AUTH_FILTER START");

        StringBuilder requestBody = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            log.error("Request body 읽기 실패: {}", e.getMessage());
        }
        // JSON body 확인
        log.info("로그인필터 <request body 확인>: {}", requestBody.toString());
        JSONObject jsonObject = new JSONObject(requestBody.toString());
        String loginId = jsonObject.optString("loginId");
        String password = jsonObject.optString("password");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password, null);

        try{
            return authenticationManager.authenticate(authToken);
        } catch (AuthenticationException e){
            log.error("로그인필터 <로그인시도> 실패 : {}", e.getMessage());
            throw new CustomException(ErrorCode.LOGIN_CHECK_FAIL);
        }
    }

    // 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급.)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        log.info("SUCCESS_AUTH START");

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        String loginId = securityUser.getUsername();
        long expiredMs = 12L * 60 * 60 * 1000; // 12시간


        Optional<String> roleOptional = securityUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst();

        if (roleOptional.isPresent()) {
            String role = roleOptional.get();
            log.info("SUCCESS_AUTH_ROLE >> loginId : {}, ROLE : {}", loginId, role);

                if ("PENDING".equals(role)) {
                    unsuccessfulAuthentication(request, response,
                            new AuthenticationException("Pending Account") {
                                {
                                    initCause(new CustomException(ErrorCode.PENDING_ACCOUNT));
                                }
                            });
                    return;
                } else if ("UNAPPROVED".equals(role)) {
                    unsuccessfulAuthentication(request, response,
                            new AuthenticationException("Unapproved Account") {
                                {
                                    initCause(new CustomException(ErrorCode.UNAPPROVED_ACCOUNT));
                                }
                            });
                    return;
                }

                String token = jwtUtil.createJwt(loginId, role, expiredMs);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"token\":\"" + token + "\", \"role\":\"" + role + "\"}");
                response.getWriter().flush();

                log.info("SUCCESS_AUTH FINISH");
            } else {
                log.warn("No role assigned to user: {}", loginId);
                unsuccessfulAuthentication(request, response,
                        new AuthenticationException("No Role Assigned") {
                        });
            }
        }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorDetails = new HashMap<>();

        if (failed.getCause() instanceof CustomException customException) {
            errorDetails.put("message", customException.getMessage());
            errorDetails.put("code", customException.getErrorCode().name());
            errorDetails.put("status", customException.getStatus());
        } else {
            errorDetails.put("message", "로그인에 실패했습니다");
            errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());
        }

        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
};
