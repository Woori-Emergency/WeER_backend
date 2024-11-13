package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.UserDTO;
import com.weer.weer_backend.entity.User;
import com.weer.weer_backend.enums.Approve;
import com.weer.weer_backend.enums.Role;
import com.weer.weer_backend.exception.CustomException;
import com.weer.weer_backend.exception.ErrorCode;
import com.weer.weer_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(UserDTO userDTO) {
        User user = User.builder()
                .loginId(userDTO.getLoginId())
                .name(userDTO.getName())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .tel(userDTO.getTel())
                .role(Role.MEMBER)
                .approved(Approve.PENDING)
                .certificate(userDTO.getCertificate())
                .organization(userDTO.getOrganization())
                .build();
        try {
            userRepository.save(user);
        }  catch (Exception e) {
            throw new CustomException(ErrorCode.DATABASE_FAIL);
        }
    }

    @Override
    public boolean isLoginIdDuplicate(String loginId) {

        return userRepository.existsByLoginId(loginId);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}
