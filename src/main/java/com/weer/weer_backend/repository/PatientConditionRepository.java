package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.PatientCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientConditionRepository extends JpaRepository<PatientCondition, Long>{
}
