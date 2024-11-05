package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 관련 추가 메서드 정의 가능
    List<User> findByApproved(boolean approved);  // 'approved' 필드로 사용자 조회
    List<User> findByApprovedTrue();
}

