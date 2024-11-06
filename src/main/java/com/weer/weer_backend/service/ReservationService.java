package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.ReservationDTO;
import com.weer.weer_backend.entity.Reservation;
import com.weer.weer_backend.enums.ReservationStatus;
import com.weer.weer_backend.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;

    // 병원에서 승인
    public void reservationApprove(ReservationDTO reservationDTO) {
        Reservation reservation = reservationRepository.findByReservationId(reservationDTO.getReservationId());
        reservation.changeStatus(ReservationStatus.APPROVED);
        reservationRepository.save(reservation);
    }

    // 병원에서 거절
    public void reservationReject(ReservationDTO reservationDTO) {
        Reservation reservation = reservationRepository.findByReservationId(reservationDTO.getReservationId());
        reservation.changeStatus(ReservationStatus.DECLINED);
        reservationRepository.save(reservation);
    }

    // 병원 별 예약 리스트
    public List<Reservation> getHospitalReservation(Long hospitalid){
        return reservationRepository.findAllByHospitalId(hospitalid);
    }
}