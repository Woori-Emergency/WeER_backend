package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.SecurityUser;
import com.weer.weer_backend.entity.User;
import com.weer.weer_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(loginId)
        .orElseThrow(() -> new UsernameNotFoundException(loginId));

    return new SecurityUser(user);
  }
}
