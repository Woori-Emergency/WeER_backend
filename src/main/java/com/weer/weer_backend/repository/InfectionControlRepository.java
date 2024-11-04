package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.InfectionControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfectionControlRepository extends JpaRepository<InfectionControl, Long> {
    // 감염 관리 관련 추가 메서드 정의 가능
}
