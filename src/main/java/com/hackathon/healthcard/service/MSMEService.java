package com.hackathon.healthcard.service;

import com.hackathon.healthcard.dto.*;
import com.hackathon.healthcard.entity.MSME;

import java.util.UUID;

public interface MSMEService {
    MSME createMsme(MsmeCreateRequest request);
    MSME getMsmeById(UUID id);
    MSME login(String mobileNumber);
    HealthCardResponse getHealthCard(UUID id);
    RevenueAnalyticsResponse getRevenueAnalytics(UUID id);
    LoanAssessmentResponse assessLoan(LoanAssessmentRequest request);
}
