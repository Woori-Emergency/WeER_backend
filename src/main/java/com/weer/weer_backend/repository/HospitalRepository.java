package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.Hospital;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    // 병원 관련 추가 메서드 정의 가능
    List<Hospital> findByCityAndState(String city, String state);
    Optional<Hospital> findByHpid(String hpid);

    @Query("SELECT h FROM Hospital h WHERE " +
        "ST_Distance_Sphere(POINT(:longitude, :latitude), POINT(h.longitude, h.latitude)) <= :range " +
        "ORDER BY ST_Distance_Sphere(POINT(:longitude, :latitude), POINT(h.longitude, h.latitude)) ASC")
    List<Hospital> findRangeHospital(@Param("latitude") Double latitude,
        @Param("longitude") Double longitude,
        @Param("range") double range);


}
