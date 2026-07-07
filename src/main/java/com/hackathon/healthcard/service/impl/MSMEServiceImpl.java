package com.hackathon.healthcard.service.impl;

import com.hackathon.healthcard.dto.*;
import com.hackathon.healthcard.entity.FinancialData;
import com.hackathon.healthcard.entity.FinancialHealth;
import com.hackathon.healthcard.entity.MSME;
import com.hackathon.healthcard.exception.BadRequestException;
import com.hackathon.healthcard.exception.ResourceNotFoundException;
import com.hackathon.healthcard.exception.UnauthorizedException;
import com.hackathon.healthcard.repository.FinancialDataRepository;
import com.hackathon.healthcard.repository.FinancialHealthRepository;
import com.hackathon.healthcard.repository.MSMERepository;
import com.hackathon.healthcard.service.MSMEService;
import com.hackathon.healthcard.service.engine.FinancialHealthEngine;
import com.hackathon.healthcard.service.mock.MockDataService;
import com.hackathon.healthcard.util.MockDataConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class MSMEServiceImpl implements MSMEService {

    private final MSMERepository msmeRepository;
    private final FinancialHealthRepository financialHealthRepository;
    private final FinancialDataRepository financialDataRepository;
    private final MockDataService mockDataService;
    private final FinancialHealthEngine financialHealthEngine;

    @Override
    public MSME createMsme(MsmeCreateRequest request) {
        MSME msme = MSME.builder()
                .businessName(request.getBusinessName())
                .pan(request.getPan())
                .gstNumber(request.getGstNumber())
                .industryType(request.getIndustryType())
                .mobileNumber(request.getMobileNumber())
                .build();
        MSME savedMsme = msmeRepository.save(msme);

        // Auto-sync mock data
        mockDataService.syncGstData(savedMsme);
        mockDataService.syncUpiData(savedMsme);
        mockDataService.syncEpfoData(savedMsme);
        mockDataService.syncBankStatementData(savedMsme);
        mockDataService.syncAaData(savedMsme);

        // Auto-calculate health score
        financialHealthEngine.calculateHealthScore(savedMsme);

        return savedMsme;
    }

    @Override
    public MSME getMsmeById(UUID id) {
        return msmeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MSME not found with ID: " + id));
    }

    @Override
    public MSME login(String mobileNumber) {
        return msmeRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new UnauthorizedException("MSME not found with mobile number: " + mobileNumber));
    }

    @Override
    public HealthCardResponse getHealthCard(UUID id) {
        MSME msme = getMsmeById(id);
        FinancialHealth health = financialHealthRepository.findByMsmeId(id)
                .orElseThrow(() -> new BadRequestException("Health score not generated yet. Please wait for sync."));
                
        String statusText;
        if (health.getHealthScore() >= 800) {
            statusText = "Excellent business health. You are in the top tier of MSMEs in your sector.";
        } else if (health.getHealthScore() >= 600) {
            statusText = "Stable business health with room for improvement.";
        } else {
            statusText = "Critical business health. Immediate attention required to financials.";
        }
        
        String loanDesc = health.getRecommendedLoanAmount().compareTo(BigDecimal.ZERO) > 0 
                ? "Based on your Health Score, you are eligible for immediate disbursement."
                : "Currently not eligible for automated loans due to risk factors.";

        return HealthCardResponse.builder()
                .companyName(msme.getBusinessName())
                .profileImageUrl("https://lh3.googleusercontent.com/aida-public/AB6AXuCMWFXvsH8kOw-IWGcffc9SK9FFI7TCyKcXm5PJG4PE5RsahKulmMNfnPVNo9Z7MN3WWAPXTYIl86I-rbAvn7mvMTo6CKSKNDtplykrEBKHdW2ffs-a6zykGEDs7dt2smnihTgJ8KGgQD7fQHb0gqJ5sCJQfLCC0qunTQg4X6UYNFVyK6BWDx9yv6ui271THAF-8vRL_iZiT71gnfeaxOBn8VDvK8klAwQRF0pjdTP7kjpi7MiamwFR")
                .overallScore((health.getHealthScore() * 100) / 1000)
                .maxScore(100)
                .statusText(statusText)
                .badges(Arrays.asList(
                        HealthCardResponse.BadgeDto.builder().text(health.getRiskCategory().name() + " RISK").icon("verified").type("PRIMARY").build(),
                        HealthCardResponse.BadgeDto.builder().text(health.getLoanEligibility().name().replace("_", " ")).icon("trending_up").type("TERTIARY").build()
                ))
                .scoreBreakdowns(MockDataConstants.generateHealthCardMockData(msme).getScoreBreakdowns()) // Keep static for charts
                .strengths(health.getHealthScore() >= 600 ? Arrays.asList("Consistent Revenue", "Healthy digital adoption") : Arrays.asList("Registered Entity"))
                .risks(health.getHealthScore() < 800 ? Arrays.asList("Cash reserves need monitoring") : Arrays.asList("No immediate risks detected"))
                .loanOffer(HealthCardResponse.LoanOfferDto.builder()
                        .title("Business Loan Up to ₹" + health.getRecommendedLoanAmount().divide(BigDecimal.valueOf(100_000), 2, java.math.RoundingMode.HALF_UP) + "L")
                        .description(loanDesc)
                        .buttonText(health.getRecommendedLoanAmount().compareTo(BigDecimal.ZERO) > 0 ? "Apply Now" : "View Requirements")
                        .build())
                .build();
    }

    @Override
    public com.hackathon.healthcard.dto.RevenueAnalyticsResponse getRevenueAnalytics(UUID id) {
        MSME msme = getMsmeById(id);
        List<FinancialData> history = financialDataRepository.findByMsmeIdOrderByRecordMonthAsc(id);
        
        if (history.isEmpty()) {
            throw new BadRequestException("No financial data found. Please run sync first.");
        }

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalBankBalance = BigDecimal.ZERO;
        
        List<RevenueAnalyticsResponse.RevenueTrendDto> revenueTrend = new java.util.ArrayList<>();
        List<RevenueAnalyticsResponse.CashFlowDto> cashFlows = new java.util.ArrayList<>();
        List<RevenueAnalyticsResponse.GstTaxableValueDto> gstTaxableValues = new java.util.ArrayList<>();

        for (FinancialData data : history) {
            BigDecimal gstRev = data.getGstRevenue() != null ? data.getGstRevenue() : BigDecimal.ZERO;
            BigDecimal bankBal = data.getAvgBankBalance() != null ? data.getAvgBankBalance() : BigDecimal.ZERO;
            
            totalRevenue = totalRevenue.add(gstRev);
            totalBankBalance = totalBankBalance.add(bankBal);
            
            // Extract month string
            String monthLabel = data.getRecordMonth().substring(data.getRecordMonth().length() - 2); 
            
            revenueTrend.add(RevenueAnalyticsResponse.RevenueTrendDto.builder()
                    .month(monthLabel)
                    .value(gstRev.divide(BigDecimal.valueOf(100_000), 2, java.math.RoundingMode.HALF_UP).doubleValue())
                    .build());
                    
            cashFlows.add(RevenueAnalyticsResponse.CashFlowDto.builder()
                    .month(monthLabel)
                    .inflow(gstRev.divide(BigDecimal.valueOf(100_000), 2, java.math.RoundingMode.HALF_UP).doubleValue())
                    .outflow(gstRev.multiply(BigDecimal.valueOf(0.7)).divide(BigDecimal.valueOf(100_000), 2, java.math.RoundingMode.HALF_UP).doubleValue()) // Simulated outflow
                    .build());
                    
            gstTaxableValues.add(RevenueAnalyticsResponse.GstTaxableValueDto.builder()
                    .month(monthLabel)
                    .taxableValue("₹" + gstRev.divide(BigDecimal.valueOf(100_000), 2, java.math.RoundingMode.HALF_UP) + "L")
                    .progress(0.9)
                    .build());
        }

        String formattedTotalRev = "₹" + totalRevenue.divide(BigDecimal.valueOf(1_00_00_000), 2, java.math.RoundingMode.HALF_UP) + " Cr";
        String formattedAvgBal = "₹" + totalBankBalance.divide(BigDecimal.valueOf(history.size() * 100_000), 2, java.math.RoundingMode.HALF_UP) + " L";

        return RevenueAnalyticsResponse.builder()
                .aiInsights(Arrays.asList(
                        "Revenue mapped across " + history.size() + " months of synced data.",
                        "Average monthly bank balance maintained at " + formattedAvgBal + ".",
                        "GST filings indicate consistent turnover."
                ))
                .totalRevenue(formattedTotalRev)
                .revenueGrowth("Synced Live")
                .netCashFlow(formattedAvgBal)
                .gstTurnover(formattedTotalRev)
                .revenueTrend(revenueTrend)
                .cashFlows(cashFlows)
                .gstTaxableValues(gstTaxableValues)
                .digitalAdoptionPercentage(75)
                .costCenters(MockDataConstants.generateRevenueAnalyticsMockData(msme).getCostCenters()) // Re-using static
                .dsoDays(34) // Re-using static
                .dsoTrend("-4 days vs LY")
                .build();
    }

    @Override
    public LoanAssessmentResponse assessLoan(LoanAssessmentRequest request) {
        int score = 0;
        java.util.List<String> insights = new java.util.ArrayList<>();

        // 0. Fetch Financial Health Data First
        FinancialHealth health = financialHealthRepository.findByMsmeId(request.getId())
                .orElseThrow(() -> new BadRequestException("Health score not generated yet. Please wait for sync."));

        insights.add("Alternative Data Health Score is " + health.getHealthScore() + "/1000 (" + health.getRiskCategory() + " Risk).");

        if (health.getHealthScore() >= 800) {
            score += 50;
        } else if (health.getHealthScore() >= 600) {
            score += 25;
        } else {
            score -= 50;
            insights.add("Critical financial health severely restricts loan eligibility.");
        }

        // 1. Evaluate Business Age
        String ageStr = request.getBusinessAge() != null ? request.getBusinessAge().toLowerCase() : "";
        if (ageStr.contains("year")) {
            try {
                int years = Integer.parseInt(ageStr.replaceAll("[^0-9]", ""));
                if (years > 5) {
                    score += 35;
                    insights.add("Established business history (" + years + " years) provides high stability.");
                } else if (years >= 2) {
                    score += 25;
                    insights.add("Moderate business history (" + years + " years) shows stable growth.");
                } else {
                    score += 10;
                    insights.add("New business (" + years + " years), higher risk profile.");
                }
            } catch (NumberFormatException e) {
                score += 15;
            }
        } else {
            score += 15; // default fallback
        }

        // 2. Evaluate Loan Purpose
        String purpose = request.getLoanPurpose() != null ? request.getLoanPurpose().toLowerCase() : "";
        if (purpose.contains("expansion") || purpose.contains("asset")) {
            score += 35;
            insights.add("Purpose of '" + request.getLoanPurpose() + "' is structured and growth-oriented.");
        } else if (purpose.contains("working capital")) {
            score += 20;
            insights.add("Working capital requests carry moderate risk.");
        } else {
            score += 10;
            insights.add("Unstructured loan purpose increases risk.");
        }

        // 3. Evaluate Working Capital Requirement
        String wc = request.getWcRequirement() != null ? request.getWcRequirement().toLowerCase() : "";
        if (wc.contains("low")) {
            score += 30;
            insights.add("Low working capital dependency indicates strong cash reserves.");
        } else if (wc.contains("medium")) {
            score += 20;
            insights.add("Moderate working capital dependency.");
        } else {
            score += 10;
            insights.add("High working capital dependency indicates liquidity constraints.");
        }

        // Determine Tiers
        boolean isEligible;
        String recommendedLoan;
        String riskLevel;
        String healthStatus;
        String aiInsightsText;
        int confidence;

        // Using health object from DB to influence strictness
        boolean systemRejected = "REJECTED".equals(health.getLoanEligibility().name());

        if (score > 100 && !systemRejected) {
            isEligible = true;
            recommendedLoan = request.getRequestedLoan(); // Full amount
            riskLevel = "Low";
            healthStatus = "Healthy";
            confidence = 92;
            aiInsightsText = "Excellent credit profile. The business age, structured loan purpose, and high health score make this a low-risk application.";
        } else if (score >= 50 && !systemRejected) {
            isEligible = true;
            try {
                long reqAmount = Long.parseLong(request.getRequestedLoan());
                // Cap by Recommended Loan Amount from Engine
                long engineRecommended = health.getRecommendedLoanAmount() != null ? health.getRecommendedLoanAmount().longValue() : 0L;
                long approvedAmount = Math.min((long)(reqAmount * 0.7), engineRecommended);
                recommendedLoan = String.valueOf(approvedAmount > 0 ? approvedAmount : engineRecommended);
            } catch (Exception e) {
                recommendedLoan = "Partial Approval";
            }
            riskLevel = "Medium";
            healthStatus = "Stable";
            confidence = 75;
            aiInsightsText = "Average credit profile. The application is eligible but recommended for a reduced loan amount based on financial health constraints.";
        } else {
            isEligible = false;
            recommendedLoan = "0";
            riskLevel = "High";
            healthStatus = "Critical";
            confidence = 88;
            aiInsightsText = systemRejected 
                ? "High-risk profile. The Alternative Data Engine rejected the loan due to low financial health score."
                : "High-risk profile. The combination of short business history or unstructured loan purpose makes this ineligible.";
        }

        return LoanAssessmentResponse.builder()
                .isEligible(isEligible)
                .recommendedLoan(recommendedLoan)
                .riskLevel(riskLevel)
                .healthStatus(healthStatus)
                .confidencePercentage(confidence)
                .aiInsightsText(aiInsightsText)
                .aiInsightsList(insights)
                .build();
    }
}
