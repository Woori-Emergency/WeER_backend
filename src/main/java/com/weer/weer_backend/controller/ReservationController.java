package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.ReservationDTO;
import com.weer.weer_backend.entity.Reservation;
import com.weer.weer_backend.enums.ReservationStatus;
import com.weer.weer_backend.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ToDo : 병원 예약 시스템 개발

    // 병원에서 승인
    /*
    ReservationStatus -> approve로 변경
     */
    @PostMapping("/hospitals/approve")
    public ResponseEntity<ReservationStatus> approveHospital(ReservationDTO reservationDTO){
        reservationService.reservationApprove(reservationDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationDTO.getReservationStatus());
    }

    // 병원에서 거절
    /*
    ReservationStatus -> decline으로 변경
     */
    @PostMapping("/hospitals/decline")
    public ResponseEntity<ReservationStatus> declineHospital(ReservationDTO reservationDTO){
        reservationService.reservationReject(reservationDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationDTO.getReservationStatus());
    }

    // 병원 별 예약 리스트
    @GetMapping("/hospitals/reservations/{hospitalid}")
    public ResponseEntity<Object> showHospitalReservation(@PathVariable(name = "hospitalid") Long hospitalid){
        List<Reservation> myReservation = reservationService.getHospitalReservation(hospitalid);
        return ResponseEntity.status(HttpStatus.OK).body(myReservation);
    }
}
