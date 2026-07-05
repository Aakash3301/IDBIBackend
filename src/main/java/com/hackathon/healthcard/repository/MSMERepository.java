package com.hackathon.healthcard.repository;

import com.hackathon.healthcard.entity.MSME;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MSMERepository extends JpaRepository<MSME, UUID> {
    Optional<MSME> findByMobileNumber(String mobileNumber);
}
