package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 예약 관련 추가 메서드 정의 가능
}
