package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.MedicalDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalDepartmentRepository extends JpaRepository<MedicalDepartment, Long> {
    // 의료 부서 관련 추가 메서드 정의 가능
}
