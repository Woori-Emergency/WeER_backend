package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.UserResponseDTO;
import com.weer.weer_backend.dto.UserUpdateDTO;
import com.weer.weer_backend.entity.User;
import com.weer.weer_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 사용자 전체 리스트의 특정 값 조회
    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDTO>> getApprovedUserList() {
        List<UserResponseDTO> users = userService.getAllApprovedUsers();
        return ResponseEntity.ok(users);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO updateDTO) {
        User updatedUser = userService.updateUser(id, updateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // 회원가입 요청 리스트 조회 (PENDING 상태)
    @GetMapping("/signup-requests/")
    public ResponseEntity<List<User>> getSignupRequests() {
        List<User> signupRequests = userService.getSignupRequests();
        return ResponseEntity.ok(signupRequests);
    }

    // 요청된 회원가입 승인/반려
    @PostMapping("/approve-signup/")
    public ResponseEntity<String> approveSignup(@RequestParam Long userId, @RequestParam boolean approve) {
        userService.approveSignup(userId, approve);
        return ResponseEntity.ok("회원가입 " + (approve ? "승인" : "반려") + "되었습니다.");
    }
}
