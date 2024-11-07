package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.ReservationDTO;
import com.weer.weer_backend.dto.ReservationRequestDto;
import com.weer.weer_backend.entity.Reservation;
import com.weer.weer_backend.enums.ReservationStatus;
import com.weer.weer_backend.repository.ReservationRepository;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    // 병원에서 승인
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void reservationApprove(ReservationDTO reservationDTO) {
        Reservation reservation = reservationRepository.findByReservationId(reservationDTO.getReservationId());
        List<Reservation> reservationList = reservationRepository.findAllByPatientconditionid(
            reservationDTO.getReservationId());
        long count = reservationList.stream()
            .filter(e -> e.getReservationStatus().equals(ReservationStatus.APPROVED))
            .count();

        if (count > 0) {
            reservationList.stream()
                .filter(r -> !r.getReservationStatus().equals(ReservationStatus.APPROVED))
                .forEach(r -> reservationRepository.save(r.changeStatus(ReservationStatus.CANCELLED)));
        } else if (!reservation.getReservationStatus().equals(ReservationStatus.APPROVED)) {
            reservationRepository.save(reservation.changeStatus(ReservationStatus.APPROVED));
        }
    }


    // 병원에서 거절
    @Transactional
    public void reservationReject(ReservationDTO reservationDTO) {
        Reservation reservation = reservationRepository.findByReservationId(reservationDTO.getReservationId());
        reservation.changeStatus(ReservationStatus.DECLINED);
        reservationRepository.save(reservation);
    }

    // 병원 별 예약 리스트
    public List<Reservation> getHospitalReservation(Long hospitalid){
        return reservationRepository.findAllByHospitalId(hospitalid);
    }

    public String reservationRequest(ReservationRequestDto reservationRequestDto){
        try{
            reservationRepository.save(reservationRequestDto.from());
            return "success";
        }catch (Exception e){
            return "error";
        }
    }
}