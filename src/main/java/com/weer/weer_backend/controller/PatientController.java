package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.ApiResponse;
import com.weer.weer_backend.dto.PatientConditionDTO;
import com.weer.weer_backend.dto.PatientConditionResponseDTO;
import com.weer.weer_backend.dto.ReservationDTO;
import com.weer.weer_backend.entity.PatientCondition;
import com.weer.weer_backend.repository.PatientConditionRepository;
import com.weer.weer_backend.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientController {
    private final PatientService patientService;
    private final PatientConditionRepository patientConditionRepository;

    public PatientController(PatientService patientService, PatientConditionRepository patientConditionRepository) {
        this.patientService = patientService;
        this.patientConditionRepository = patientConditionRepository;
    }

    // 환자 상태 저장
    @PostMapping("/hospital/patient")
    public ResponseEntity<ApiResponse<PatientConditionResponseDTO>> createPatientCondition(
            @RequestParam Long userId,
            @Valid @RequestBody PatientConditionDTO dto
    ) {
        try {
            PatientCondition savedEntity = patientService.createPatientCondition(userId, dto);
            PatientConditionResponseDTO responseDTO = PatientConditionResponseDTO.fromEntity(savedEntity);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(responseDTO, "환자 상태가 성공적으로 등록되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."));
        }
    }

    // 환자 밑에 등록된 병원 정보 가져오기
    @GetMapping("hospital/patient/{patient_id}")
    public ResponseEntity<ApiResponse<List<ReservationDTO>>> getPatientReservationList(
            @PathVariable(name = "patient_id") Long patientconditionid) {
        try {
            List<ReservationDTO> reservations = patientService.getPatientReservationList(patientconditionid);

            return ResponseEntity.ok(ApiResponse.<List<ReservationDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("환자의 예약 정보를 성공적으로 조회했습니다.")
                    .data(reservations)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<ReservationDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("환자의 예약 정보 조회 중 오류가 발생했습니다.")
                            .build());
        }
    }

    //유저별 등록된 환자 리스트
    @GetMapping("/user/reservation/{user_id}")
    public ResponseEntity<ApiResponse<List<PatientConditionResponseDTO>>> getPatientList(
            @PathVariable(name = "user_id") Long userId) {
        try {
            List<PatientConditionResponseDTO> patients = patientService.getPatientConditionList(userId);
            if (patients.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.<List<PatientConditionResponseDTO>>builder()
                        .status(HttpStatus.NO_CONTENT.value())
                        .message("등록된 환자가 없습니다.")
                        .data(patients)
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.<List<PatientConditionResponseDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("환자 목록을 성공적으로 조회했습니다.")
                    .data(patients)
                    .build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<PatientConditionResponseDTO>>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("잘못된 사용자 ID입니다.")
                            .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<List<PatientConditionResponseDTO>>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("환자 목록 조회 중 오류가 발생했습니다.")
                            .build());
        }
    }
}
