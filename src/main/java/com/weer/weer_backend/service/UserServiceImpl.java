package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.UserResponseDTO;
import com.weer.weer_backend.dto.UserUpdateDTO;
import com.weer.weer_backend.entity.User;
import com.weer.weer_backend.enums.Approve;
import com.weer.weer_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponseDTO> getAllApprovedUsers() {
        return userRepository.findByApproved(Approve.APPROVED).stream()
                .map(user -> new UserResponseDTO(
                        user.getName(),
                        user.getLoginId(),
                        user.getTel(),
                        user.getOrganization(),
                        user.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.updateWith(userUpdateDTO);

        return UserResponseDTO.from(existingUser);
    }

    @Override
    public List<User> getSignupRequests() {
        return userRepository.findByApproved(Approve.PENDING);  // 승인 대기 중인 사용자 조회
    }

    @Override
    public void approveSignup(Long userId, boolean approve) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Approve approved = approve ? Approve.APPROVED : Approve.UNAPPROVED;

        user = User.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .name(user.getName())
                .password(user.getPassword())
                .role(user.getRole())
                .email(user.getEmail())
                .tel(user.getTel())
                .certificate(user.getCertificate())
                .organization(user.getOrganization())
                .approved(approved)
                .build();

        userRepository.save(user); // 승인 여부 업데이트 후 저장
    }
}
