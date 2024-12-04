package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.ERAnnouncement;
import com.weer.weer_backend.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ERAnnouncementRepository extends JpaRepository<ERAnnouncement, Long> {
    // 응급실 공지 관련 추가 메서드 정의 가능
  List<ERAnnouncement> findAllByHospitalId(Hospital hospitalId);
}
