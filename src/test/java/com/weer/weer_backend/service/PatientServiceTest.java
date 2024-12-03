package com.weer.weer_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.weer.weer_backend.dto.PatientConditionDTO;
import com.weer.weer_backend.dto.PatientConditionResponseDTO;
import com.weer.weer_backend.dto.ReservationDTO;
import com.weer.weer_backend.entity.PatientCondition;
import com.weer.weer_backend.entity.Reservation;
import com.weer.weer_backend.enums.AgeGroup;
import com.weer.weer_backend.enums.Gender;
import com.weer.weer_backend.enums.ReservationStatus;
import com.weer.weer_backend.enums.TransportStatus;
import com.weer.weer_backend.exception.CustomException;
import com.weer.weer_backend.exception.ErrorCode;
import com.weer.weer_backend.repository.PatientConditionRepository;
import com.weer.weer_backend.repository.ReservationRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PatientServiceTest {

  @Mock
  private PatientConditionRepository patientConditionRepository;

  @Mock
  private ReservationRepository reservationRepository;

  @InjectMocks
  private PatientService patientService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createPatientCondition_success() {
    // Given
    Long userId = 1L;
    PatientConditionDTO dto = PatientConditionDTO.builder()
        .gender(Gender.MALE)
        .ageGroup(AgeGroup.FIFTIES)
        .build();
    PatientCondition patientCondition = dto.toEntity(userId);

    when(patientConditionRepository.save(any(PatientCondition.class))).thenReturn(patientCondition);

    // When
    PatientCondition result = patientService.createPatientCondition(userId, dto);

    // Then
    assertNotNull(result);
    assertEquals(Gender.MALE, result.getGender());
    assertEquals(AgeGroup.FIFTIES, result.getAgeGroup());
    verify(patientConditionRepository, times(1)).save(any(PatientCondition.class));
  }

  @Test
  void updatePatientTransportStatusToCompleted_success() {
    // Given
    Long patientId = 1L;
    PatientCondition existingCondition = PatientCondition.builder()
        .patientconditionid(patientId)
        .userId(1L)
        .transportStatus(TransportStatus.COMPLETED)
        .build();

    PatientCondition updatedCondition = existingCondition.toBuilder()
        .transportStatus(TransportStatus.COMPLETED)
        .build();

    when(patientConditionRepository.findById(patientId)).thenReturn(Optional.of(existingCondition));
    when(patientConditionRepository.save(any(PatientCondition.class))).thenReturn(updatedCondition);

    // When
    PatientCondition result = patientService.updatePatientTransportStatusToCompleted(patientId);

    // Then
    assertNotNull(result);
    assertEquals(TransportStatus.COMPLETED, result.getTransportStatus());
    verify(patientConditionRepository, times(1)).findById(patientId);
    verify(patientConditionRepository, times(1)).save(any(PatientCondition.class));
  }

  @Test
  void updatePatientTransportStatusToCompleted_patientNotFound() {
    // Given
    Long patientId = 1L;

    when(patientConditionRepository.findById(patientId)).thenReturn(Optional.empty());

    // When / Then
    CustomException exception = assertThrows(CustomException.class, () ->
        patientService.updatePatientTransportStatusToCompleted(patientId));

    assertEquals(ErrorCode.PATIENT_NOT_FOUND, exception.getErrorCode());
    verify(patientConditionRepository, times(1)).findById(patientId);
    verify(patientConditionRepository, times(0)).save(any(PatientCondition.class));
  }

  @Test
  void getPatientReservationList_success() {
    // Given
    Long patientId = 1L;
    Reservation reservation = Reservation.builder()
        .hospitalId(1L)
        .patientconditionid(patientId)
        .reservationStatus(ReservationStatus.PENDING)
        .build();

    when(reservationRepository.findAllByPatientconditionid(patientId)).thenReturn(List.of(reservation));

    // When
    List<ReservationDTO> reservationDTOList = patientService.getPatientReservationList(patientId);

    // Then
    assertNotNull(reservationDTOList);
    assertEquals(1, reservationDTOList.size());
    assertEquals(patientId, reservationDTOList.get(0).getPatientconditionId());
    verify(reservationRepository, times(1)).findAllByPatientconditionid(patientId);
  }

  @Test
  void getPatientConditionList_success() {
    // Given
    Long userId = 1L;
    PatientCondition patientCondition = PatientCondition.builder()
        .patientconditionid(1L)
        .userId(userId)
        .gender(Gender.FEMALE)
        .ageGroup(AgeGroup.FORTIES)
        .build();

    when(patientConditionRepository.findAllByUserId(userId)).thenReturn(List.of(patientCondition));

    // When
    List<PatientConditionResponseDTO> patientConditionResponseDTOList = patientService.getPatientConditionList(userId);

    // Then
    assertNotNull(patientConditionResponseDTOList);
    assertEquals(1, patientConditionResponseDTOList.size());
    assertEquals(Gender.FEMALE, patientConditionResponseDTOList.get(0).getGender());
    verify(patientConditionRepository, times(1)).findAllByUserId(userId);
  }
}
