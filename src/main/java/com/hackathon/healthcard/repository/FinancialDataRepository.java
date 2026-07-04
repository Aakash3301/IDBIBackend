package com.hackathon.healthcard.repository;

import com.hackathon.healthcard.entity.FinancialData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FinancialDataRepository extends JpaRepository<FinancialData, UUID> {
    
    // Find all financial data for a specific MSME, ordered by month chronologically
    // Spring Data JPA automatically implements this based on the method name
    List<FinancialData> findByMsmeIdOrderByRecordMonthAsc(UUID msmeId);
}
