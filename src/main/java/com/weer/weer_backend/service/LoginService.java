package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.UserDTO;

public interface LoginService {
    UserDTO authenticate(String loginId, String password) throws Exception;

    void signUp(UserDTO userDTO);

    public boolean isLoginIdDuplicate(String loginId);

    public boolean isEmailDuplicate(String email);

}
