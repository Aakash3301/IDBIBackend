package com.hackathon.healthcard.repository;

import com.hackathon.healthcard.entity.FinancialHealth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FinancialHealthRepository extends JpaRepository<FinancialHealth, UUID> {
    
    // Fetch the calculated health score record for an MSME
    Optional<FinancialHealth> findByMsmeId(UUID msmeId);
}
