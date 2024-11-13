package com.weer.weer_backend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weer.weer_backend.dto.SecurityUser;
import com.weer.weer_backend.dto.TokenDto;
import com.weer.weer_backend.enums.Approve;
import com.weer.weer_backend.exception.CustomException;
import com.weer.weer_backend.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final TokenProvider tokenProvider;  // Updated to use TokenProvider
  private final ObjectMapper objectMapper = new ObjectMapper();

  public LoginFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;

    setFilterProcessesUrl("/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    log.info("ATTEMPT_AUTH_FILTER START");

    StringBuilder requestBody = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(request.getInputStream()))) {
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

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password);

    try{
      return authenticationManager.authenticate(authToken);
    } catch (AuthenticationException e){
        try {
            failAuthenticationWithCustomException(request, response, ErrorCode.LOGIN_CHECK_FAIL, "Wrong ID/PW");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
  return null;
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) throws IOException {
    log.info("SUCCESS_AUTH START");

    SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
    String loginId = securityUser.getUsername();

    Approve statusOptional = securityUser.getUser().getApproved();

    if (statusOptional.equals(Approve.APPROVED)) {
      Approve status = statusOptional;
      log.info("SUCCESS_AUTH_ROLE >> loginId : {}, ROLE : {}", loginId, status);

      TokenDto tokenDto = tokenProvider.generateToken(loginId, authentication);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write(
          "{\"accessToken\":\"" + tokenDto.getAccessToken() + "\", \"refreshToken\":\""
              + tokenDto.getRefreshToken() + "\", \"grantType\":\"" + tokenDto.getGrantType()
              + "\", \"role\":\"" + tokenDto.getRole()
              + "\"}");
      response.getWriter().flush();

      log.info("SUCCESS_AUTH FINISH");
    }
    else if (statusOptional.equals(Approve.PENDING)){
      failAuthenticationWithCustomException(request, response, ErrorCode.PENDING_ACCOUNT, "Pending Account");
      log.info("Pending Account ERROR");
    }
    else if (statusOptional.equals(Approve.UNAPPROVED)){
      failAuthenticationWithCustomException(request, response, ErrorCode.UNAPPROVED_ACCOUNT, "Unapproved Account");
      log.info("UnApproved Account ERROR");
    }
    else {
      log.warn("No role assigned to user: {}", loginId);
      unsuccessfulAuthentication(request, response,
          new BadCredentialsException("No Role Assigned"));
    }
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException failed) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json;charset=UTF-8");

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
  private void failAuthenticationWithCustomException(HttpServletRequest request, HttpServletResponse response, ErrorCode errorCode, String message) throws IOException {
    unsuccessfulAuthentication(request, response, new BadCredentialsException(message) {
      {
        initCause(new CustomException(errorCode));
      }
    });
  }

}
