package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.UserResponseDTO;
import com.weer.weer_backend.dto.UserUpdateDTO;
import com.weer.weer_backend.entity.User;
import com.weer.weer_backend.enums.Approve;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    // 승인된 사용자 리스트 조회
    List<UserResponseDTO> getAllApprovedUsers();

    // 사용자 정보 업데이트
    UserResponseDTO updateUser(Long userId, UserUpdateDTO userUpdateDTO);

    // 회원가입 요청 리스트 조회 (PENDING 상태인 사용자만 반환)
    List<User> getSignupRequests();

    // 회원가입 요청 승인/반려 처리
    void approveSignup(Long userId, Approve approve);

    String getHospitalName(Long id);
}
