package com.hackathon.healthcard.service.engine.impl;

import com.hackathon.healthcard.entity.FinancialData;
import com.hackathon.healthcard.entity.FinancialHealth;
import com.hackathon.healthcard.entity.MSME;
import com.hackathon.healthcard.entity.enums.LoanEligibility;
import com.hackathon.healthcard.entity.enums.RiskCategory;
import com.hackathon.healthcard.repository.FinancialDataRepository;
import com.hackathon.healthcard.repository.FinancialHealthRepository;
import com.hackathon.healthcard.service.engine.FinancialHealthEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialHealthEngineImpl implements FinancialHealthEngine {

    private final FinancialDataRepository financialDataRepository;
    private final FinancialHealthRepository financialHealthRepository;

    @Override
    @Transactional
    public FinancialHealth calculateHealthScore(MSME msme) {
        List<FinancialData> history = financialDataRepository.findByMsmeIdOrderByRecordMonthAsc(msme.getId());
        
        if (history.isEmpty()) {
            throw new IllegalStateException("No financial data found for MSME. Please generate mock data first.");
        }

        BigDecimal totalRevenue = BigDecimal.ZERO;
        int totalUpiVolume = 0;
        BigDecimal totalBankBalance = BigDecimal.ZERO;

        for (FinancialData data : history) {
            totalRevenue = totalRevenue.add(data.getGstRevenue());
            totalUpiVolume += data.getUpiVolume();
            totalBankBalance = totalBankBalance.add(data.getAvgBankBalance());
        }

        BigDecimal avgMonthlyRevenue = totalRevenue.divide(BigDecimal.valueOf(history.size()), 2, RoundingMode.HALF_UP);
        BigDecimal avgBankBalance = totalBankBalance.divide(BigDecimal.valueOf(history.size()), 2, RoundingMode.HALF_UP);
        int avgUpiVolume = totalUpiVolume / history.size();

        int score = 0;

        if (avgMonthlyRevenue.compareTo(BigDecimal.valueOf(15_00_000)) > 0) score += 400;
        else if (avgMonthlyRevenue.compareTo(BigDecimal.valueOf(5_00_000)) > 0) score += 300;
        else score += 150;

        if (avgUpiVolume > 1000) score += 300;
        else if (avgUpiVolume > 500) score += 200;
        else score += 100;

        BigDecimal balanceRatio = avgBankBalance.divide(avgMonthlyRevenue, 2, RoundingMode.HALF_UP);
        if (balanceRatio.compareTo(BigDecimal.valueOf(0.40)) > 0) score += 300;
        else if (balanceRatio.compareTo(BigDecimal.valueOf(0.20)) > 0) score += 200;
        else score += 100;

        RiskCategory riskCategory;
        if (score >= 800) riskCategory = RiskCategory.LOW;
        else if (score >= 600) riskCategory = RiskCategory.MEDIUM;
        else riskCategory = RiskCategory.HIGH;

        LoanEligibility loanEligibility;
        BigDecimal recommendedLoanAmount = BigDecimal.ZERO;

        if (riskCategory == RiskCategory.LOW) {
            loanEligibility = LoanEligibility.ELIGIBLE;
            recommendedLoanAmount = avgMonthlyRevenue.multiply(BigDecimal.valueOf(3));
        } else if (riskCategory == RiskCategory.MEDIUM) {
            loanEligibility = LoanEligibility.REVIEW;
            recommendedLoanAmount = avgMonthlyRevenue;
        } else {
            loanEligibility = LoanEligibility.REJECTED;
        }

        FinancialHealth healthProfile = financialHealthRepository.findByMsmeId(msme.getId())
                .orElse(new FinancialHealth());

        healthProfile.setMsme(msme);
        healthProfile.setHealthScore(score);
        healthProfile.setRiskCategory(riskCategory);
        healthProfile.setLoanEligibility(loanEligibility);
        healthProfile.setRecommendedLoanAmount(recommendedLoanAmount);

        return financialHealthRepository.save(healthProfile);
    }
}
