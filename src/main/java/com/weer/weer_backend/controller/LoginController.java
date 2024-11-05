package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.UserDTO;
import com.weer.weer_backend.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        try {
            UserDTO authenticatedUser = loginService.authenticate(userDTO.getLoginId(), userDTO.getPassword());
            HttpSession session = request.getSession();
            session.setAttribute("user", authenticatedUser);
            return ResponseEntity.ok("로그인 성공!"); // 성공 메시지 반환
        }
        catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 잘못되었습니다.");
        }
        catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("회원가입이 아직 승인되지 않았습니다.");
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다.");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO) {
        try {
            loginService.signUp(userDTO);
            return ResponseEntity.ok("회원가입 성공!"); // 성공 메시지 반환
        }
        catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("ID 중복");
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

