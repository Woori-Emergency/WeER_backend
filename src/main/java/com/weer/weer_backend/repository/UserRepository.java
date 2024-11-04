package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 사용자 관련 추가 메서드 정의 가능

}
