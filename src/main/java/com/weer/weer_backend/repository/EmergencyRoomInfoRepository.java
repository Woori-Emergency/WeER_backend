package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.EmergencyRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyRoomInfoRepository extends JpaRepository<EmergencyRoomInfo, Long> {
    // 응급실 정보 관련 추가 메서드 정의 가능
}
