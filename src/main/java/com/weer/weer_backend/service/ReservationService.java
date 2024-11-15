package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.PatientConditionResponseDTO;
import com.weer.weer_backend.dto.ReservationDTO;
import com.weer.weer_backend.dto.ReservationRequestDto;
import com.weer.weer_backend.entity.PatientCondition;
import com.weer.weer_backend.entity.Reservation;
import com.weer.weer_backend.enums.ReservationStatus;
import com.weer.weer_backend.repository.PatientConditionRepository;
import com.weer.weer_backend.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final PatientConditionRepository patientConditionRepository;
    // 병원에서 승인
    @Transactional
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
            return e.getMessage();
        }
    }

    // 환자들의 건강 정보 리스트
    public List<PatientConditionResponseDTO> getPatientConditionList(List<Long> patientsConditionId) {
        List<PatientCondition> patientConditions = patientConditionRepository.findAllByPatientconditionidIn(patientsConditionId);
        return patientConditions.stream()
                .map(PatientConditionResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}