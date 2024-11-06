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

    // 예약 생성
    /*
    (1) 가용 병상 상관 없이
    (2) Transaction null (예외처리)
    예외 처리 ) 상태가 pending인지 확인 -> 진행되는 거고,
    ** Status에 취소 넣기

     */

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

    // 유저 별 예약 리스트 확인
    // 세션으로 보는 게 나을 수도 있다.
    public List<Reservation> getUserReservation(Long loginid){
        return reservationRepository.findAllByUserId(loginid);
    }

    // 병원 별 예약 리스트
    public List<Reservation> getHospitalReservation(Long hospitalid){
        return reservationRepository.findAllByHospitalId(hospitalid);
    }
}