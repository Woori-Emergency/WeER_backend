package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.PatientCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientConditionRepository extends JpaRepository<PatientCondition, Long>{
    List<PatientCondition> findAllByUserId(Long userId);
}
