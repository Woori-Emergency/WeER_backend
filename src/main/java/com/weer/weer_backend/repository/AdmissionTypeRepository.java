package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.AdmissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionTypeRepository extends JpaRepository<AdmissionType, Long> {
    // 입원 유형 관련 추가 메서드 정의 가능
}
