package com.weer.weer_backend.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.weer.weer_backend.dto.UserDTO;
import com.weer.weer_backend.entity.User;
import com.weer.weer_backend.exception.CustomException;
import com.weer.weer_backend.exception.ErrorCode;
import com.weer.weer_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private LoginServiceImpl loginService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSignUp_Success() {
    // given
    UserDTO userDTO = UserDTO.builder()
        .loginId("testUser")
        .name("Test User")
        .password("password")
        .email("test@example.com")
        .tel("123-456-7890")
        .certificate("certificate")
        .organization("organization")
        .build();

    when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
    when(userRepository.existsByLoginId(userDTO.getLoginId())).thenReturn(false);
    when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");

    // when
    assertDoesNotThrow(() -> loginService.signUp(userDTO));

    // then
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void testSignUp_DuplicateLoginIdOrEmail() {
    // given
    UserDTO userDTO = UserDTO.builder()
        .loginId("duplicateUser")
        .email("duplicate@example.com")
        .build();

    when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

    // when
    CustomException exception = assertThrows(CustomException.class, () -> loginService.signUp(userDTO));

    // then
    assertEquals(ErrorCode.DUPLICATED_SIGNUP, exception.getErrorCode());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void testSignUp_DatabaseFail() {
    // given
    UserDTO userDTO = UserDTO.builder()
        .loginId("testUser")
        .name("Test User")
        .password("password")
        .email("test@example.com")
        .tel("123-456-7890")
        .certificate("certificate")
        .organization("organization")
        .build();

    when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
    when(userRepository.existsByLoginId(userDTO.getLoginId())).thenReturn(false);
    when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
    doThrow(new RuntimeException()).when(userRepository).save(any(User.class));

    // when
    CustomException exception = assertThrows(CustomException.class, () -> loginService.signUp(userDTO));

    // then
    assertEquals(ErrorCode.DATABASE_FAIL, exception.getErrorCode());
  }

  @Test
  void testIsLoginIdDuplicate() {
    // given
    String loginId = "testUser";
    when(userRepository.existsByLoginId(loginId)).thenReturn(true);

    // when
    boolean result = loginService.isLoginIdDuplicate(loginId);

    // then
    assertTrue(result);
    verify(userRepository, times(1)).existsByLoginId(loginId);
  }

  @Test
  void testIsEmailDuplicate() {
    // given
    String email = "test@example.com";
    when(userRepository.existsByEmail(email)).thenReturn(true);

    // when
    boolean result = loginService.isEmailDuplicate(email);

    // then
    assertTrue(result);
    verify(userRepository, times(1)).existsByEmail(email);
  }
}
