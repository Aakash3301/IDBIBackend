package com.hackathon.healthcard.service;

import com.hackathon.healthcard.dto.HealthCardResponse;
import com.hackathon.healthcard.dto.MsmeCreateRequest;
import com.hackathon.healthcard.entity.MSME;

import java.util.UUID;

public interface MSMEService {
    MSME createMsme(MsmeCreateRequest request);
    MSME getMsmeById(UUID id);
    MSME login(String mobileNumber);
    HealthCardResponse getHealthCard(UUID id);
    com.hackathon.healthcard.dto.RevenueAnalyticsResponse getRevenueAnalytics(UUID id);
    com.hackathon.healthcard.dto.LoanAssessmentResponse assessLoan(com.hackathon.healthcard.dto.LoanAssessmentRequest request);
}
