package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.Emergency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmergencyRepository extends JpaRepository<Emergency, Long> {

    Optional<Emergency> findByHpid(String hpid);
    boolean existsByHpid(String hpid);

}