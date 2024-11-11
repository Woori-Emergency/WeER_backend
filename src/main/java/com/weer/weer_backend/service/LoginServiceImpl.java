package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.LoginForm;
import com.weer.weer_backend.dto.UserDTO;
import com.weer.weer_backend.entity.User;
import com.weer.weer_backend.enums.Role;
import com.weer.weer_backend.exception.CustomException;
import com.weer.weer_backend.exception.ErrorCode;
import com.weer.weer_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public UserDTO authenticate(LoginForm loginForm){
        User user = userRepository.findByLoginId(loginForm.getLoginId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if(!passwordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_CHECK_FAIL);
        }
        if (!user.getApproved()){
            throw new CustomException(ErrorCode.UNAPPROVED_ACCOUNT);
        }
        userDetailsService.loadUserByUsername(user.getLoginId());

        return convertToDTO(user);
    }

    @Override
    public void signUp(UserDTO userDTO) {
        // 비동기식으로 이미 LoginId와 Email의 중복은 체크를 한 후 회원가입이 가능
        // if문이 과연 필요한가?
        /*if (userRepository.existsByLoginId(userDTO.getLoginId())) {
            throw new IllegalArgumentException("ID 중복");
        }*/
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        }
        User user = User.builder()
                .loginId(userDTO.getLoginId())
                .name(userDTO.getName())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .tel(userDTO.getTel())
                .role(Role.MEMBER)
                .approved(false)
                .certificate(userDTO.getCertificate())
                .organization(userDTO.getOrganization())
                .build();
        userRepository.save(user);
    }

    @Override
    public boolean isLoginIdDuplicate(String loginId) {

        return userRepository.existsByLoginId(loginId);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }


    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .email(user.getEmail())
                .tel(user.getTel())
                .organization(user.getOrganization())
                .build();
    }
}
