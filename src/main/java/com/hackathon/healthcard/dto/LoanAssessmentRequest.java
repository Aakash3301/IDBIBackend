package com.hackathon.healthcard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanAssessmentRequest {
    private UUID id;
    private String requestedLoan;
    private String loanPurpose;
    private String businessAge;
    private String wcRequirement;
}
