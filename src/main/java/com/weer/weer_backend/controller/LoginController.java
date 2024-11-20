package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.ApiResponse;
import com.weer.weer_backend.dto.UserDTO;
import com.weer.weer_backend.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/signup")
    public ApiResponse<String> signup(@RequestBody UserDTO userDTO){
        loginService.signUp(userDTO);
        return ApiResponse.success("회원가입이 요청되었습니다3.");
    }
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("/main");
        return ApiResponse.success("logout success");
    }

    // 비동기식 ID 중복체크"
    @GetMapping("/check-login-id")
    public ApiResponse<Boolean> checkLoginId(@RequestParam String loginId) throws Exception{
        return ApiResponse.success(loginService.isLoginIdDuplicate(loginId));
    }

    // 비동기식 Email 중복체크
    @GetMapping("/check-email")
    public ApiResponse<Boolean> checkEmail(@RequestParam String email) {
        return ApiResponse.success(loginService.isEmailDuplicate(email));
    }
}

