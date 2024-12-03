package com.weer.weer_backend.controller;

import com.weer.weer_backend.dto.*;
import com.weer.weer_backend.entity.PatientCondition;
import com.weer.weer_backend.exception.CustomException;
import com.weer.weer_backend.exception.ErrorCode;
import com.weer.weer_backend.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // 환자 상태 저장
    @PostMapping("/hospital/patient")
    public ResponseEntity<ApiResponse<PatientConditionResponseDTO>> createPatientCondition(
            @AuthenticationPrincipal SecurityUser user, @RequestBody PatientConditionDTO dto
    ) {
        try {
            Long userId = user.getUser().getUserId();
            PatientCondition savedEntity = patientService.createPatientCondition(userId, dto);
            PatientConditionResponseDTO responseDTO = PatientConditionResponseDTO.fromEntity(savedEntity);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(responseDTO, "환자 상태가 성공적으로 등록되었습니다."));
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.PATIENT_SAVE_FAIL);
        }
    }

    // 환자의 transportStatus를 COMPLETED로 업데이트하는 엔드포인트
    @PatchMapping("/hospital/patient/{patient_id}/complete")
    public ResponseEntity<ApiResponse<PatientConditionResponseDTO>> completePatientTransportStatus(
            @PathVariable(name = "patient_id") Long patientId) {
        try {
            PatientCondition updatedCondition = patientService.updatePatientTransportStatusToCompleted(patientId);
            PatientConditionResponseDTO responseDTO = PatientConditionResponseDTO.fromEntity(updatedCondition);

            return ResponseEntity.ok(ApiResponse.success(responseDTO, "환자 이송 상태가 COMPLETED로 업데이트되었습니다."));
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.<PatientConditionResponseDTO>builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message("해당 환자를 찾을 수 없습니다.")
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.<PatientConditionResponseDTO>builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("환자 이송 상태 업데이트 중 오류가 발생했습니다.")
                            .build());
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
                    .result(reservations)
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
    @GetMapping("/user/reservation")
    public ResponseEntity<ApiResponse<List<PatientConditionResponseDTO>>> getPatientList(
            @AuthenticationPrincipal SecurityUser user) {
        Long userId = user.getUser().getUserId();
        try {
            List<PatientConditionResponseDTO> patients = patientService.getPatientConditionList(userId);
            if (patients.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.<List<PatientConditionResponseDTO>>builder()
                        .status(HttpStatus.NO_CONTENT.value())
                        .message("등록된 환자가 없습니다.")
                        .result(patients)
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.<List<PatientConditionResponseDTO>>builder()
                    .status(HttpStatus.OK.value())
                    .message("환자 목록을 성공적으로 조회했습니다.")
                    .result(patients)
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