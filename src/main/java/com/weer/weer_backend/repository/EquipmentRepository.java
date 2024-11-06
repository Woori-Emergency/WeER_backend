package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    // 장비 관련 추가 메서드 정의 가능
}
