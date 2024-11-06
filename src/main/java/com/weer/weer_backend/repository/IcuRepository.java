package com.weer.weer_backend.repository;

import com.weer.weer_backend.entity.Icu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IcuRepository extends JpaRepository<Icu, Long> {

}
