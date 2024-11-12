package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.Hospital;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    // 병원 관련 추가 메서드 정의 가능
    List<Hospital> findByCityAndState(String city, String state);
    boolean existsByHpid(String hpid); // hpid로 중복 여부 확인
    Optional<Hospital> findByHpid(String hpid);

}
