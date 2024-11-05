package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.UserResponseDTO;
import com.weer.weer_backend.dto.UserUpdateDTO;
import com.weer.weer_backend.entity.User;
import com.weer.weer_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    // 승인된 사용자만 조회하여 DTO로 반환
    public List<UserResponseDTO> getAllApprovedUsers() {
        return userRepository.findByApprovedTrue().stream()
                .map(user -> new UserResponseDTO(
                        user.getName(),
                        user.getLoginId(),
                        user.getTel(),
                        user.getOrganization(),
                        user.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    // 사용자 업데이트 메서드
    public User updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // DTO의 필드를 사용하여 사용자 정보 업데이트
        user.setName(userUpdateDTO.getName());
        user.setTel(userUpdateDTO.getTel());
        user.setOrganization(userUpdateDTO.getOrganization());

        // 변경된 사용자 정보 저장
        return userRepository.save(user);
    }

    // 회원가입 요청 리스트 조회 (PENDING 상태인 사용자만 반환)
    public List<User> getSignupRequests() {
        return userRepository.findByApproved(false);  // 'approved'가 false인 사용자 조회
    }

    // 회원가입 요청 승인/반려 처리
    public void approveSignup(Long userId, boolean approve) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setApproved(approve);
        userRepository.save(user);
    }
}
