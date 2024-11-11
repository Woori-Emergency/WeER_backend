package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.LoginForm;
import com.weer.weer_backend.dto.UserDTO;

public interface LoginService {
    UserDTO authenticate(LoginForm loginForm) throws Exception;

    void signUp(UserDTO userDTO);

    public boolean isLoginIdDuplicate(String loginId);

    public boolean isEmailDuplicate(String email);

}
