package com.hackathon.healthcard.service;

import com.hackathon.healthcard.dto.DashboardResponseDto;
import com.hackathon.healthcard.entity.MSME;
import com.hackathon.healthcard.entity.FinancialData;
import com.hackathon.healthcard.entity.FinancialHealth;
import com.hackathon.healthcard.exception.BadRequestException;
import com.hackathon.healthcard.repository.FinancialDataRepository;
import com.hackathon.healthcard.repository.FinancialHealthRepository;
import com.hackathon.healthcard.util.MockDataConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MSMEService msmeService;
    private final FinancialDataRepository financialDataRepository;
    private final FinancialHealthRepository financialHealthRepository;

    public DashboardResponseDto getDashboardData(UUID msmeId) {
        MSME msme = msmeService.getMsmeById(msmeId);
        
        FinancialHealth health = financialHealthRepository.findByMsmeId(msmeId)
                .orElseThrow(() -> new BadRequestException("Health score not generated yet. Please wait for sync."));
                
        List<FinancialData> history = financialDataRepository.findByMsmeIdOrderByRecordMonthAsc(msmeId);
        
        if (history.isEmpty()) {
            throw new BadRequestException("No financial data found. Please run sync first.");
        }

        // Calculate Average Monthly Revenue
        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (FinancialData data : history) {
            totalRevenue = totalRevenue.add(data.getGstRevenue());
        }
        BigDecimal avgMonthlyRevenue = totalRevenue.divide(BigDecimal.valueOf(history.size()), 2, java.math.RoundingMode.HALF_UP);
        
        // Format for display
        String formattedRevenue = "₹" + avgMonthlyRevenue.divide(BigDecimal.valueOf(100_000), 2, java.math.RoundingMode.HALF_UP) + "L";
        
        String loanAmountText = "₹" + health.getRecommendedLoanAmount().divide(BigDecimal.valueOf(100_000), 2, java.math.RoundingMode.HALF_UP) + " Lakh";
        if (health.getRecommendedLoanAmount().compareTo(BigDecimal.ZERO) == 0) {
            loanAmountText = "Not Eligible";
        }

        return DashboardResponseDto.builder()
                .companyName(msme.getBusinessName())
                .sector(msme.getIndustryType() != null ? msme.getIndustryType().name() : "Manufacturing Sector")
                .hasNotifications(true)
                .profileImageUrl("https://lh3.googleusercontent.com/aida-public/AB6AXuCMWFXvsH8kOw-IWGcffc9SK9FFI7TCyKcXm5PJG4PE5RsahKulmMNfnPVNo9Z7MN3WWAPXTYIl86I-rbAvn7mvMTo6CKSKNDtplykrEBKHdW2ffs-a6zykGEDs7dt2smnihTgJ8KGgQD7fQHb0gqJ5sCJQfLCC0qunTQg4X6UYNFVyK6BWDx9yv6ui271THAF-8vRL_iZiT71gnfeaxOBn8VDvK8klAwQRF0pjdTP7kjpi7MiamwFR")
                .healthScore(DashboardResponseDto.HealthScoreDto.builder()
                        .score((health.getHealthScore() * 100) / 1000) // Scale to 100 for frontend
                        .maxScore(100)
                        .statusText(health.getRiskCategory() == com.hackathon.healthcard.entity.enums.RiskCategory.LOW ? "Healthy Business" : 
                                    health.getRiskCategory() == com.hackathon.healthcard.entity.enums.RiskCategory.MEDIUM ? "Stable Business" : "Critical Profile")
                        .tags(Arrays.asList(health.getRiskCategory().name() + " RISK", health.getLoanEligibility().name().replace("_", " ")))
                        .build())
                .kpis(Arrays.asList(
                        DashboardResponseDto.KpiDto.builder()
                                .id("revenue")
                                .icon("payments")
                                .title("Avg Monthly Revenue")
                                .value(formattedRevenue)
                                .trend("Based on 6 mo")
                                .trendDirection("neutral")
                                .colorType("primary")
                                .build(),
                        DashboardResponseDto.KpiDto.builder()
                                .id("cash_flow")
                                .icon("account_balance_wallet")
                                .title("Risk Category")
                                .value(health.getRiskCategory().name())
                                .trend("System Assessed")
                                .trendDirection(health.getRiskCategory() == com.hackathon.healthcard.entity.enums.RiskCategory.LOW ? "up" : "down")
                                .colorType("tertiary")
                                .build(),
                        DashboardResponseDto.KpiDto.builder()
                                .id("gst_compliance")
                                .icon("gavel")
                                .title("GST Compliance")
                                .value("Synced")
                                .trend("Data Confirmed")
                                .trendDirection("neutral")
                                .colorType("secondary")
                                .build(),
                        DashboardResponseDto.KpiDto.builder()
                                .id("upi_growth")
                                .icon("rocket_launch")
                                .title("Score Range")
                                .value(String.valueOf(health.getHealthScore()))
                                .trend("out of 1000")
                                .trendDirection("up")
                                .colorType("primary")
                                .build()
                ))
                .loanEligibility(DashboardResponseDto.LoanEligibilityDto.builder()
                        .status(health.getLoanEligibility().name())
                        .amountText(loanAmountText)
                        .confidencePercentage(health.getHealthScore() > 800 ? 95 : (health.getHealthScore() > 600 ? 75 : 30))
                        .build())
                .quickActions(MockDataConstants.generateDashboardMockData(msme).getQuickActions()) // Re-using static quick actions config
                .build();
    }
}
