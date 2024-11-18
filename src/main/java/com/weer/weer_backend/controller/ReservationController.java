package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.ApiResponse;
import com.weer.weer_backend.dto.PatientConditionResponseDTO;
import com.weer.weer_backend.dto.ReservationDTO;
import com.weer.weer_backend.dto.ReservationRequestDto;
import com.weer.weer_backend.entity.Reservation;
import com.weer.weer_backend.enums.ReservationStatus;
import com.weer.weer_backend.service.ReservationService;
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
    public ApiResponse<ReservationStatus> approveHospital(@RequestBody ReservationDTO reservationDTO){
        reservationService.reservationApprove(reservationDTO);
        return ApiResponse.success(reservationDTO.getReservationStatus());
    }

    // 병원에서 거절
    /*
    ReservationStatus -> decline으로 변경
     */
    @PostMapping("/hospitals/decline")
    public ApiResponse<ReservationStatus> declineHospital(@RequestBody ReservationDTO reservationDTO){
        reservationService.reservationReject(reservationDTO);
        return ApiResponse.success(reservationDTO.getReservationStatus());
    }

    // 병원 별 예약 리스트
    @GetMapping("/hospitals/reservations/{hospitalid}")
    public ApiResponse<List<Reservation>> showHospitalReservation(@PathVariable(name = "hospitalid") Long hospitalid){
        List<Reservation> myReservation = reservationService.getHospitalReservation(hospitalid);

        return ApiResponse.success(myReservation);
    }

    // 병원에 예약 요청을 보낸 환자들의 건강 정보 리스트
    @GetMapping("/hospitals/patients-info/{patients}")
    public ApiResponse<List<PatientConditionResponseDTO>> showRequestedPatientsInfo(@PathVariable(name = "patients") List<Long> patientsConditionId){

        return ApiResponse.success(reservationService.getPatientConditionList(patientsConditionId));
    }

    @PostMapping("/hospital/reserve")
    public ApiResponse<String> reserveHospital(@RequestBody ReservationRequestDto reservationDTO){
        return ApiResponse.success(reservationService.reservationRequest(reservationDTO));
    }
}
