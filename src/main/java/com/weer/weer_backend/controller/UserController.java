package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.ApiResponse;
import com.weer.weer_backend.dto.UserResponseDTO;
import com.weer.weer_backend.dto.UserUpdateDTO;
import com.weer.weer_backend.entity.User;
import com.weer.weer_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 승인된 사용자 목록 조회
    @GetMapping("/list")
    public ApiResponse<List<UserResponseDTO>> getAllApprovedUsers() {
        List<UserResponseDTO> users = userService.getAllApprovedUsers();
        return ApiResponse.success(users);
    }

    // 유저 정보 수정
    @PutMapping("/update/{id}")
    public ApiResponse<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO updateDTO) {
        UserResponseDTO updatedUser = userService.updateUser(id, updateDTO);
        return ApiResponse.success(updatedUser);
    }

    // 회원가입 요청 리스트 조회 (Approved = False 상태)
    @GetMapping("/signup-request")
    public ApiResponse<List<User>> getSignupRequests() {
        List<User> signupRequests = userService.getSignupRequests();
        return ApiResponse.success(signupRequests);
    }

    // 요청된 회원가입 승인/반려
    @PostMapping("/approve-signup/{id}")
    public ApiResponse<String> approveUserSignup(@PathVariable Long id, @RequestParam boolean approve) {
        userService.approveSignup(id, approve);
        String status = approve ? "승인" : "반려";
        return ApiResponse.success("User " + status + " 성공");
    }
}
