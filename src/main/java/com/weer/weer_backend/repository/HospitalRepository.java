package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    // 병원 관련 추가 메서드 정의 가능
}
