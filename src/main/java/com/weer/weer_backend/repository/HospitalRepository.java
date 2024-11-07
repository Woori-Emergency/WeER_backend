package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.Hospital;
import com.weer.weer_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    boolean existsByHpid(String hpid); // hpid로 중복 여부 확인
    Optional<Hospital> findByHpid(String hpid);

}
