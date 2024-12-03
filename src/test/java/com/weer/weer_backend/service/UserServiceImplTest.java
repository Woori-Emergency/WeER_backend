package com.weer.weer_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.weer.weer_backend.dto.UserResponseDTO;
import com.weer.weer_backend.dto.UserUpdateDTO;
import com.weer.weer_backend.entity.User;
import com.weer.weer_backend.enums.Approve;
import com.weer.weer_backend.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllApprovedUsers() {
    User user = User.builder()
        .userId(1L)
        .loginId("user1")
        .name("John Doe")
        .tel("1234567890")
        .organization("Hospital A")
        .approved(Approve.APPROVED)
        .build();

    when(userRepository.findByApproved(Approve.APPROVED)).thenReturn(Collections.singletonList(user));

    List<UserResponseDTO> approvedUsers = userService.getAllApprovedUsers();

    assertEquals(1, approvedUsers.size());
    assertEquals("John Doe", approvedUsers.get(0).getName());
    verify(userRepository, times(1)).findByApproved(Approve.APPROVED);
  }

  @Test
  void testUpdateUser() {
    Long userId = 1L;
    UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
        .name("Updated Name")
        .tel("0987654321")
        .build();

    User existingUser = User.builder()
        .userId(userId)
        .loginId("user1")
        .name("Old Name")
        .tel("1234567890")
        .organization("Hospital A")
        .approved(Approve.APPROVED)
        .build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

    UserResponseDTO updatedUser = userService.updateUser(userId, userUpdateDTO);

    assertEquals("Updated Name", updatedUser.getName());
    assertEquals("0987654321", updatedUser.getTel());
    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void testGetSignupRequests() {
    User pendingUser = User.builder()
        .userId(2L)
        .loginId("user2")
        .name("Jane Doe")
        .approved(Approve.PENDING)
        .build();

    when(userRepository.findByApproved(Approve.PENDING)).thenReturn(Collections.singletonList(pendingUser));

    List<User> signupRequests = userService.getSignupRequests();

    assertEquals(1, signupRequests.size());
    assertEquals("Jane Doe", signupRequests.get(0).getName());
    verify(userRepository, times(1)).findByApproved(Approve.PENDING);
  }

  @Test
  void testApproveSignup() {
    Long userId = 3L;
    User user = User.builder()
        .userId(userId)
        .loginId("user3")
        .name("Mark Smith")
        .approved(Approve.PENDING)
        .build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    userService.approveSignup(userId, Approve.APPROVED);

    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void testGetHospitalName() {
    Long userId = 4L;
    User user = User.builder()
        .userId(userId)
        .loginId("user4")
        .name("Alice Brown")
        .organization("Hospital B")
        .build();

    when(userRepository.findFirstByUserId(userId)).thenReturn(Optional.of(user));

    String hospitalName = userService.getHospitalName(userId);

    assertEquals("Hospital B", hospitalName);
    verify(userRepository, times(1)).findFirstByUserId(userId);
  }
}
