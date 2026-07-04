package com.hackathon.healthcard.dto;

import com.hackathon.healthcard.entity.enums.LoanEligibility;
import com.hackathon.healthcard.entity.enums.RiskCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class HealthCardResponse {
    private UUID msmeId;
    private String businessName;
    private Integer healthScore;
    private RiskCategory riskCategory;
    private LoanEligibility loanEligibility;
    private BigDecimal recommendedLoanAmount;
    private LocalDateTime lastCalculatedAt;
}
