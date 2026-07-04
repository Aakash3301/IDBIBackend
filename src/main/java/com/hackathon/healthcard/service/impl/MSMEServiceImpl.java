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
                .build();
        return msmeRepository.save(msme);
    }

    @Override
    public MSME getMsmeById(UUID id) {
        return msmeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MSME not found with ID: " + id));
    }

    @Override
    public HealthCardResponse getHealthCard(UUID id) {
        MSME msme = getMsmeById(id);
        FinancialHealth health = financialHealthRepository.findByMsmeId(id)
                .orElseThrow(() -> new RuntimeException("Health score not generated yet. Please run calculation first."));

        return HealthCardResponse.builder()
                .msmeId(msme.getId())
                .businessName(msme.getBusinessName())
                .healthScore(health.getHealthScore())
                .riskCategory(health.getRiskCategory())
                .loanEligibility(health.getLoanEligibility())
                .recommendedLoanAmount(health.getRecommendedLoanAmount())
                .lastCalculatedAt(health.getLastCalculatedAt())
                .build();
    }
}
