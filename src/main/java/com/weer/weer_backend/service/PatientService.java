package com.weer.weer_backend.service;

import com.weer.weer_backend.dto.PatientConditionDTO;
import com.weer.weer_backend.dto.PatientConditionResponseDTO;
import com.weer.weer_backend.dto.ReservationDTO;
import com.weer.weer_backend.entity.PatientCondition;
import com.weer.weer_backend.entity.Reservation;
import com.weer.weer_backend.enums.TransportStatus;
import com.weer.weer_backend.exception.CustomException;
import com.weer.weer_backend.exception.ErrorCode;
import com.weer.weer_backend.repository.PatientConditionRepository;
import com.weer.weer_backend.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientConditionRepository patientConditionRepository;
    private final ReservationRepository reservationRepository;

    // 환자 정보 저장하기
    @Transactional
    public PatientCondition createPatientCondition(Long userId, PatientConditionDTO dto) {
        validateRequiredFields(dto);
        PatientCondition newPatientCondition = dto.toEntity(userId);
        return patientConditionRepository.save(newPatientCondition);
    }

    private void validateRequiredFields(PatientConditionDTO dto) {
        if (dto.getGender() == null) {
            throw new IllegalArgumentException("성별은 필수 입력값입니다.");
        }
        if (dto.getAgeGroup() == null) {
            throw new IllegalArgumentException("연령대는 필수 입력값입니다.");
        }
    }

    // 환자의 transportStatus를 COMPLETED로 업데이트
    @Transactional
    public PatientCondition updatePatientTransportStatusToCompleted(Long patientId) {
        PatientCondition patientCondition = patientConditionRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        // 기존 객체를 toBuilder로 복사하고 transportStatus만 변경
        PatientCondition updatedCondition = patientCondition.toBuilder()
                .transportStatus(TransportStatus.COMPLETED)  // 상태를 COMPLETED로 변경
                .build();

        return patientConditionRepository.save(updatedCondition);
    }

    // 환자별 예약 정보 리스트 가져오기
    public List<ReservationDTO> getPatientReservationList(Long patientId) {
        List<Reservation> reservations = reservationRepository.findAllByPatientconditionid(patientId);
        return reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 유저별 등록된 환자 정보 가져오기
    public List<PatientConditionResponseDTO> getPatientConditionList(Long userId) {
        List<PatientCondition> patientConditions = patientConditionRepository.findAllByUserId(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));
        return patientConditions.stream()
                .map(PatientConditionResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .hospitalId(reservation.getHospitalId())
                .patientconditionId(reservation.getPatientconditionid())
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}