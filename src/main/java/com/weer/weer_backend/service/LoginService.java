package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.UserDTO;

public interface LoginService {

    public void signUp(UserDTO userDTO);

    public boolean isLoginIdDuplicate(String loginId);

    public boolean isEmailDuplicate(String email);

}
