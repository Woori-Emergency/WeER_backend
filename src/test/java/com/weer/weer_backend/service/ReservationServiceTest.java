package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.PatientConditionResponseDTO;
import com.weer.weer_backend.dto.ReservationDTO;
import com.weer.weer_backend.dto.ReservationRequestDto;
import com.weer.weer_backend.entity.PatientCondition;
import com.weer.weer_backend.entity.Reservation;
import com.weer.weer_backend.enums.ConsciousnessLevel;
import com.weer.weer_backend.enums.ReservationStatus;
import com.weer.weer_backend.repository.PatientConditionRepository;
import com.weer.weer_backend.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private PatientConditionRepository patientConditionRepository;

  @InjectMocks
  private ReservationService reservationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testReservationApprove() {
    // given
    ReservationDTO reservationDTO = ReservationDTO.builder()
        .reservationId(1L)
        .build();

    Reservation reservation = Reservation.builder()
        .reservationId(1L)
        .reservationStatus(ReservationStatus.PENDING)
        .build();

    List<Reservation> reservationList = Arrays.asList(
        Reservation.builder()
            .reservationId(2L)
            .patientconditionid(1L)
            .reservationStatus(ReservationStatus.PENDING)
            .build(),
        Reservation.builder()
            .reservationId(3L)
            .patientconditionid(1L)
            .reservationStatus(ReservationStatus.APPROVED)
            .build()
    );

    when(reservationRepository.findByReservationId(1L)).thenReturn(reservation);
    when(reservationRepository.findAllByPatientconditionid(1L)).thenReturn(reservationList);

    // when
    reservationService.reservationApprove(reservationDTO);

    // then
    verify(reservationRepository, times(1)).save(any(Reservation.class));
  }

  @Test
  void testReservationReject() {
    // given
    ReservationDTO reservationDTO = ReservationDTO.builder()
        .reservationId(1L)
        .build();

    Reservation reservation = Reservation.builder()
        .reservationId(1L)
        .reservationStatus(ReservationStatus.PENDING)
        .build();

    when(reservationRepository.findByReservationId(1L)).thenReturn(reservation);

    // when
    reservationService.reservationReject(reservationDTO);

    // then
    assertEquals(ReservationStatus.DECLINED, reservation.getReservationStatus());
    verify(reservationRepository, times(1)).save(reservation);
  }

  @Test
  void testGetHospitalReservation() {
    // given
    Long hospitalId = 1L;
    List<Reservation> reservationList = Arrays.asList(
        Reservation.builder()
            .reservationId(1L)
            .hospitalId(hospitalId)
            .build(),
        Reservation.builder()
            .reservationId(2L)
            .hospitalId(hospitalId)
            .build()
    );

    when(reservationRepository.findAllByHospitalId(hospitalId)).thenReturn(reservationList);

    // when
    List<Reservation> result = reservationService.getHospitalReservation(hospitalId);

    // then
    assertEquals(2, result.size());
    verify(reservationRepository, times(1)).findAllByHospitalId(hospitalId);
  }

  @Test
  void testReservationRequest() {
    // given
    ReservationRequestDto reservationRequestDto = ReservationRequestDto.builder()
        .patientconditionId(1L)
        .hospitalId(1L)
        .build();

    // when
    String result = reservationService.reservationRequest(reservationRequestDto);

    // then
    assertEquals("success", result);
    verify(reservationRepository, times(1)).save(any(Reservation.class));
  }

  @Test
  void testGetPatientConditionList() {
    // given
    List<Long> patientsConditionId = Arrays.asList(1L, 2L);
    List<PatientCondition> patientConditions = Arrays.asList(
        PatientCondition.builder()
            .patientconditionid(1L)
            .consciousnessLevel(ConsciousnessLevel.ALERT)
            .build(),
        PatientCondition.builder()
            .patientconditionid(2L)
            .consciousnessLevel(ConsciousnessLevel.PAIN)
            .build()
    );

    when(patientConditionRepository.findAllByPatientconditionidIn(patientsConditionId)).thenReturn(patientConditions);

    // when
    List<PatientConditionResponseDTO> result = reservationService.getPatientConditionList(patientsConditionId);

    // then
    assertEquals(2, result.size());
    assertEquals(ConsciousnessLevel.ALERT, result.get(0).getConsciousnessLevel());
    assertEquals(ConsciousnessLevel.PAIN, result.get(1).getConsciousnessLevel());
    verify(patientConditionRepository, times(1)).findAllByPatientconditionidIn(patientsConditionId);
  }
}
