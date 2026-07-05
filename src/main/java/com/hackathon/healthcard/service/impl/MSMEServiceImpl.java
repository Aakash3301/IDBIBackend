package com.hackathon.healthcard.service.impl;

import com.hackathon.healthcard.dto.HealthCardResponse;
import com.hackathon.healthcard.dto.MsmeCreateRequest;
import com.hackathon.healthcard.entity.FinancialHealth;
import com.hackathon.healthcard.entity.MSME;
import com.hackathon.healthcard.repository.FinancialHealthRepository;
import com.hackathon.healthcard.repository.MSMERepository;
import com.hackathon.healthcard.service.MSMEService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MSMEServiceImpl implements MSMEService {

    private final MSMERepository msmeRepository;
    private final FinancialHealthRepository financialHealthRepository;

    @Override
    public MSME createMsme(MsmeCreateRequest request) {
        MSME msme = MSME.builder()
                .businessName(request.getBusinessName())
                .pan(request.getPan())
                .gstNumber(request.getGstNumber())
                .industryType(request.getIndustryType())
                .mobileNumber(request.getMobileNumber())
                .build();
        return msmeRepository.save(msme);
    }

    @Override
    public MSME getMsmeById(UUID id) {
        return msmeRepository.findById(id)
                .orElseThrow(() -> new com.hackathon.healthcard.exception.ResourceNotFoundException("MSME not found with ID: " + id));
    }

    @Override
    public MSME login(String mobileNumber) {
        return msmeRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new com.hackathon.healthcard.exception.UnauthorizedException("MSME not found with mobile number: " + mobileNumber));
    }

    @Override
    public HealthCardResponse getHealthCard(UUID id) {
        MSME msme = getMsmeById(id);
        return com.hackathon.healthcard.util.MockDataConstants.generateHealthCardMockData(msme);
    }

    @Override
    public com.hackathon.healthcard.dto.RevenueAnalyticsResponse getRevenueAnalytics(UUID id) {
        MSME msme = getMsmeById(id);
        return com.hackathon.healthcard.util.MockDataConstants.generateRevenueAnalyticsMockData(msme);
    }
}
