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
import java.util.Objects;

@Slf4j
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
            if(Objects.equals(authenticatedUser.getRole(), "Member")){
                response.sendRedirect("/main");
            }
            if (Objects.equals(authenticatedUser.getRole(), "Admin")){
                response.sendRedirect("/admin");
            }
            if (Objects.equals(authenticatedUser.getRole(), "Hostpital")){
                response.sendRedirect("/hospital-admin");
            }
        }
        catch (SecurityException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "아이디 또는 비밀번호가 잘못되었습니다.");
        }
        catch (IllegalStateException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "회원가입이 아직 승인되지 않았습니다.");
        }
        catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "예상치 못한 오류가 발생하였습니다.");
        }
    }

    @PostMapping("/signup")
    public void signup(@RequestBody UserDTO userDTO, HttpServletResponse response) throws IOException {
        try {
            loginService.signUp(userDTO);
            response.sendRedirect("/auth/signup/success");
        }
        catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "이미 존재하는 ID");
        }
    }
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("/main");
    }

    // 비동기식 ID 중복체크
    @GetMapping("/check-login-id")
    public ResponseEntity<Boolean> checkLoginId(@RequestParam String loginId) {
        return ResponseEntity.ok(loginService.isLoginIdDuplicate(loginId));
    }

    // 비동기식 Email 중복체크
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(loginService.isEmailDuplicate(email));
    }


}

