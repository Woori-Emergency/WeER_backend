package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.LoginForm;
import com.weer.weer_backend.dto.UserDTO;
import com.weer.weer_backend.exception.CustomException;
import com.weer.weer_backend.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

//    @PostMapping("/login")
//    public void login(@RequestBody LoginForm loginForm) throws Exception {
//        loginService.authenticate(loginForm);
//    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDTO){
        loginService.signUp(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공!");
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
    public ResponseEntity<Boolean> checkLoginId(@RequestParam String loginId) throws Exception{
        return ResponseEntity.ok(loginService.isLoginIdDuplicate(loginId));
    }

    // 비동기식 Email 중복체크
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(loginService.isEmailDuplicate(email));
    }


}

