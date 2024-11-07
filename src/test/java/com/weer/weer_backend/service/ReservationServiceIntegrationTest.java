package com.weer.weer_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.weer.weer_backend.dto.ReservationDTO;
import com.weer.weer_backend.entity.Reservation;
import com.weer.weer_backend.enums.ReservationStatus;
import com.weer.weer_backend.repository.ReservationRepository;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceIntegrationTest {

  @Autowired
  private ReservationService reservationService;

  @Autowired
  private ReservationRepository reservationRepository;

  @Test
  void testReservationApproveConcurrency() throws InterruptedException {
    // Given
    Reservation reservation = Reservation.builder()
        .reservationId(1L)
        .reservationStatus(ReservationStatus.PENDING)
        .build();

    reservationRepository.save(reservation);

    ReservationDTO reservationDTO = ReservationDTO.builder()
        .reservationId(1L)
        .build();

    int threadCount = 5;
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(1);
    CountDownLatch doneLatch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      executorService.execute(() -> {
        try {
          latch.await(); // 모든 스레드가 준비될 때까지 대기
          reservationService.reservationApprove(reservationDTO);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        } finally {
          doneLatch.countDown(); // 작업 완료를 표시
        }
      });
    }

    latch.countDown(); // 모든 스레드가 동시에 시작하게 함
    doneLatch.await(); // 모든 스레드가 완료될 때까지 대기
    executorService.shutdown();

    // Then: Verify that only one approval was made
    List<Reservation> reservations = reservationRepository.findAll();
    long approvedCount = reservations.stream()
        .filter(r -> r.getReservationStatus() == ReservationStatus.APPROVED)
        .count();

    assertEquals(1, approvedCount);
  }

}

