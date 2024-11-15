package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 예약 관련 추가 메서드 정의 가능
    List<Reservation> findAllByHospitalId(Long hospitalId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Reservation findByReservationId(Long reservationId);
    List<Reservation> findAllByPatientconditionid(Long patientconditionid);
}
