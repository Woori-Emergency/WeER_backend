package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.UserDTO;
import com.weer.weer_backend.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public void login(@RequestBody UserDTO userDTO, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UserDTO authenticatedUser = loginService.authenticate(userDTO.getLoginId(), userDTO.getPassword());
            HttpSession session = request.getSession();
            session.setAttribute("user", authenticatedUser);
            response.sendRedirect("/main");
        } catch (SecurityException ex) {
            // ID 또는 비밀번호가 잘못되었을 때
            response.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        } catch (IllegalStateException ex) {
            // 사용자가 승인되지 않았을 때
            response.sendError(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        } catch (Exception ex) {
            // 기타 예외 처리
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred.");
        }
    }

    @PostMapping("/signup")
    public void signup(@RequestBody UserDTO userDTO, HttpServletResponse response) throws IOException {
        try{
            loginService.signUp(userDTO);
            response.sendRedirect("/auth/signup/success");
        } catch (IllegalArgumentException ex){
            // ID 중복
           response.sendError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        }
    }

    // 비동기식 ID 중복체크
    @GetMapping("/check-login-id")
    public ResponseEntity<Boolean> checkLoginId(@RequestParam String loginId) {
        boolean exists = loginService.isLoginIdDuplicate(loginId);
        return ResponseEntity.ok(exists);
    }

    // 비동기식 Email 중복체크
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = loginService.isEmailDuplicate(email);
        return ResponseEntity.ok(exists);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("Logged out successfully");
    }
}
