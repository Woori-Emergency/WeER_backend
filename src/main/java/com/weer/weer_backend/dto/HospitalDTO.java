package com.weer.weer_backend.dto;

import com.weer.weer_backend.entity.Emergency;
import com.weer.weer_backend.entity.Equipment;
import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.entity.Icu;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalDTO {
    private Long hospitalId;
    private Equipment equipmentId;
    private Icu icuId;
    private Emergency emergencyId;
    private String hpid;          // 병원 식별 코드 (ID)
    private String name;          // 병원 이름
    private String address;       // 병원 주소
    private String city;          // 도시
    private String state;         // 시군구
    private String tel;           // 대표 전화번호
    private String erTel;         // 응급실 전화번호
    private Double latitude;      // 위도
    private Double longitude;     // 경도
    private LocalDateTime createdAt;       // 데이터 생성 일자
    private LocalDateTime modifiedAt;      // 데이터 수정 일자

    public static HospitalDTO from(Hospital hospital) {
        return HospitalDTO.builder()
            .hospitalId(hospital.getHospitalId())
            .equipmentId(hospital.getEquipmentId())
            .icuId(hospital.getIcuId())
            .emergencyId(hospital.getEmergencyId())
            .hpid(hospital.getHpid())
            .name(hospital.getName())
            .address(hospital.getAddress())
            .city(hospital.getCity())
            .state(hospital.getState())
            .tel(hospital.getTel())
            .erTel(hospital.getErTel())
            .latitude(hospital.getLatitude())
            .longitude(hospital.getLongitude())
            .createdAt(hospital.getCreatedAt())
            .modifiedAt(hospital.getModifiedAt()).build();
    }
}
