package com.weer.weer_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.weer.weer_backend.dto.ReservationDTO;
import com.weer.weer_backend.entity.Reservation;
import com.weer.weer_backend.enums.ReservationStatus;
import com.weer.weer_backend.repository.ReservationRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ReservationServiceTest {

  @Mock
  private ReservationRepository reservationRepository;

  @InjectMocks
  private ReservationService reservationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testReservationApproveWhenAlreadyApprovedExists() {
    // Given
    ReservationDTO reservationDTO = ReservationDTO.builder()
        .reservationId(1L)
        .build();

    Reservation approvedReservation = Reservation.builder()
        .reservationStatus(ReservationStatus.APPROVED)
        .build();

    Reservation pendingReservation = Reservation.builder()
        .reservationStatus(ReservationStatus.PENDING)
        .build();

    List<Reservation> reservationList = Arrays.asList(approvedReservation, pendingReservation);

    when(reservationRepository.findByReservationId(reservationDTO.getReservationId()))
        .thenReturn(pendingReservation);
    when(reservationRepository.findAllByPatientconditionid(reservationDTO.getReservationId()))
        .thenReturn(reservationList);

    // When
    reservationService.reservationApprove(reservationDTO);

    // Then
    ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
    verify(reservationRepository, times(1)).save(reservationCaptor.capture());

    // Verify the captured Reservation object status is changed to CANCELLED
    Reservation capturedReservation = reservationCaptor.getValue();
    assertEquals(ReservationStatus.CANCELLED, capturedReservation.getReservationStatus());
  }

  @Test
  void testReservationReject() {
    // Given
    ReservationDTO reservationDTO = ReservationDTO.builder()
        .reservationId(1L)
        .build();

    Reservation reservation = Reservation.builder()
        .reservationStatus(ReservationStatus.PENDING)
        .build();

    when(reservationRepository.findByReservationId(reservationDTO.getReservationId()))
        .thenReturn(reservation);

    // When
    reservationService.reservationReject(reservationDTO);

    // Then
    assertEquals(ReservationStatus.DECLINED, reservation.getReservationStatus());
    verify(reservationRepository).save(reservation);
  }

  @Test
  void testGetHospitalReservation() {
    // Given
    Long hospitalId = 1L;
    Reservation reservation1 = Reservation.builder().build();
    Reservation reservation2 = Reservation.builder().build();
    List<Reservation> reservationList = Arrays.asList(reservation1, reservation2);

    when(reservationRepository.findAllByHospitalId(hospitalId)).thenReturn(reservationList);

    // When
    List<Reservation> result = reservationService.getHospitalReservation(hospitalId);

    // Then
    assertEquals(2, result.size());
    verify(reservationRepository).findAllByHospitalId(hospitalId);
  }

}
