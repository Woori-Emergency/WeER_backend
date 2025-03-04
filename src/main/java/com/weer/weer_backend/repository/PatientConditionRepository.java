package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.PatientCondition;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientConditionRepository extends JpaRepository<PatientCondition, Long>{
    Optional<List<PatientCondition>> findAllByUserId(Long userId);

    List<PatientCondition> findAllByPatientconditionidIn(List<Long> patientconditionIds);
}
