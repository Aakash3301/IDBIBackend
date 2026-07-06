package com.hackathon.healthcard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanAssessmentResponse {
    private boolean isEligible;
    private String recommendedLoan;
    private String riskLevel;
    private String healthStatus;
    private int confidencePercentage;
    private String aiInsightsText;
    private List<String> aiInsightsList;
}
