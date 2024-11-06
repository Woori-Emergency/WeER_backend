package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.CriticalCareFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriticalCareFacilityRepository extends JpaRepository<CriticalCareFacility, Long> {
    // 중환자실 시설 추가 메서드 정의 가능

}
