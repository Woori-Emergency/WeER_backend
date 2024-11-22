package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.User;
import java.util.Optional;

import com.weer.weer_backend.enums.Approve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByEmail(String email);

    boolean existsByLoginId(String loginId);

    boolean existsByEmail(String email);
    // 사용자 관련 추가 메서드 정의 가능
    List<User> findByApproved(Approve approved);  // 'approved' 필드로 사용자 조회

    Optional<User> findFirstByUserId(Long userId);
}

