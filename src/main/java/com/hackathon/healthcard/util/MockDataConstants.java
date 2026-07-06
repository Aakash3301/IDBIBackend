package com.hackathon.healthcard.util;

import com.hackathon.healthcard.dto.DashboardResponseDto;
import com.hackathon.healthcard.entity.MSME;

import java.util.Arrays;

public class MockDataConstants {

    public static DashboardResponseDto generateDashboardMockData(MSME msme) {
        return DashboardResponseDto.builder()
                .companyName(msme.getBusinessName())
                .sector(msme.getIndustryType() != null ? msme.getIndustryType().name() : "Manufacturing Sector")
                .hasNotifications(true)
                .profileImageUrl("https://lh3.googleusercontent.com/aida-public/AB6AXuCMWFXvsH8kOw-IWGcffc9SK9FFI7TCyKcXm5PJG4PE5RsahKulmMNfnPVNo9Z7MN3WWAPXTYIl86I-rbAvn7mvMTo6CKSKNDtplykrEBKHdW2ffs-a6zykGEDs7dt2smnihTgJ8KGgQD7fQHb0gqJ5sCJQfLCC0qunTQg4X6UYNFVyK6BWDx9yv6ui271THAF-8vRL_iZiT71gnfeaxOBn8VDvK8klAwQRF0pjdTP7kjpi7MiamwFR")
                .healthScore(DashboardResponseDto.HealthScoreDto.builder()
                        .score(87)
                        .maxScore(100)
                        .statusText("Healthy Business")
                        .tags(Arrays.asList("LOW RISK", "LOAN READY"))
                        .build())
                .kpis(Arrays.asList(
                        DashboardResponseDto.KpiDto.builder()
                                .id("revenue")
                                .icon("payments")
                                .title("Monthly Revenue")
                                .value("₹18.5L")
                                .trend("+12%")
                                .trendDirection("up")
                                .colorType("primary")
                                .build(),
                        DashboardResponseDto.KpiDto.builder()
                                .id("cash_flow")
                                .icon("account_balance_wallet")
                                .title("Cash Flow")
                                .value("Excellent")
                                .trend("Stable Profile")
                                .trendDirection("neutral")
                                .colorType("tertiary")
                                .build(),
                        DashboardResponseDto.KpiDto.builder()
                                .id("gst_compliance")
                                .icon("gavel")
                                .title("GST Compliance")
                                .value("98%")
                                .trend("On-time Filing")
                                .trendDirection("neutral")
                                .colorType("secondary")
                                .build(),
                        DashboardResponseDto.KpiDto.builder()
                                .id("upi_growth")
                                .icon("rocket_launch")
                                .title("UPI Growth")
                                .value("+18%")
                                .trend("Market expansion")
                                .trendDirection("up")
                                .colorType("primary")
                                .build()
                ))
                .loanEligibility(DashboardResponseDto.LoanEligibilityDto.builder()
                        .status("INSTANT ELIGIBILITY")
                        .amountText("₹25 Lakh")
                        .confidencePercentage(92)
                        .build())
                .quickActions(Arrays.asList(
                        DashboardResponseDto.QuickActionDto.builder()
                                .id("ai_advisor")
                                .icon("smart_toy")
                                .title("AI\nAdvisor")
                                .build(),
                        DashboardResponseDto.QuickActionDto.builder()
                                .id("health_card")
                                .icon("health_metrics")
                                .title("Financial\nHealth Card")
                                .build(),
                        DashboardResponseDto.QuickActionDto.builder()
                                .id("revenue_analytics")
                                .icon("analytics")
                                .title("Revenue\nAnalytics")
                                .build(),
                        DashboardResponseDto.QuickActionDto.builder()
                                .id("alternate_data")
                                .icon("hub")
                                .title("Data\nSources")
                                .build(),
                        DashboardResponseDto.QuickActionDto.builder()
                                .id("loan_assessment")
                                .icon("account_balance")
                                .title("Loan\nAssessment")
                                .build(),
                        DashboardResponseDto.QuickActionDto.builder()
                                .id("business_documents")
                                .icon("description")
                                .title("Business\nDocuments")
                                .build()
                ))
                .build();
    }

    public static com.hackathon.healthcard.dto.HealthCardResponse generateHealthCardMockData(MSME msme) {
        return com.hackathon.healthcard.dto.HealthCardResponse.builder()
                .companyName(msme.getBusinessName())
                .profileImageUrl("MOCKURL")
                .overallScore(87)
                .maxScore(100)
                .statusText("Your business shows high fiscal discipline and robust digital adoption. You are in the top 10% of MSMEs in your sector.")
                .badges(Arrays.asList(
                        com.hackathon.healthcard.dto.HealthCardResponse.BadgeDto.builder().text("Platinum Grade").icon("verified").type("PRIMARY").build(),
                        com.hackathon.healthcard.dto.HealthCardResponse.BadgeDto.builder().text("Improving").icon("trending_up").type("TERTIARY").build()
                ))
                .scoreBreakdowns(Arrays.asList(
                        com.hackathon.healthcard.dto.HealthCardResponse.ScoreBreakdownDto.builder().label("GST Compliance").value(95).color("PRIMARY").build(),
                        com.hackathon.healthcard.dto.HealthCardResponse.ScoreBreakdownDto.builder().label("Revenue Stability").value(92).color("PRIMARY").build(),
                        com.hackathon.healthcard.dto.HealthCardResponse.ScoreBreakdownDto.builder().label("Digital Transactions").value(91).color("PRIMARY").build(),
                        com.hackathon.healthcard.dto.HealthCardResponse.ScoreBreakdownDto.builder().label("Employee Stability").value(90).color("PRIMARY").build(),
                        com.hackathon.healthcard.dto.HealthCardResponse.ScoreBreakdownDto.builder().label("Cash Flow").value(88).color("PRIMARY").build(),
                        com.hackathon.healthcard.dto.HealthCardResponse.ScoreBreakdownDto.builder().label("Vendor Payment").value(86).color("PRIMARY").build(),
                        com.hackathon.healthcard.dto.HealthCardResponse.ScoreBreakdownDto.builder().label("Business Growth").value(83).color("PRIMARY").build()
                ))
                .strengths(Arrays.asList(
                        "GST filed consistently",
                        "Revenue growing",
                        "Healthy UPI collections"
                ))
                .risks(Arrays.asList(
                        "Cash reserves slightly low",
                        "Vendor payment cycle increasing"
                ))
                .loanOffer(com.hackathon.healthcard.dto.HealthCardResponse.LoanOfferDto.builder()
                        .title("Unlock Business Loan Up to ₹50L")
                        .description("Based on your excellent Health Score, you are pre-approved for immediate disbursement at competitive rates.")
                        .buttonText("Apply Now")
                        .build())
                .build();
    }

    public static com.hackathon.healthcard.dto.RevenueAnalyticsResponse generateRevenueAnalyticsMockData(MSME msme) {
        return com.hackathon.healthcard.dto.RevenueAnalyticsResponse.builder()
                .aiInsights(Arrays.asList(
                        "Revenue increased by 16% this quarter",
                        "Cash flow remains stable with 12% surplus",
                        "GST compliance is excellent (100% on-time filing)"
                ))
                .totalRevenue("₹1.2 Cr")
                .revenueGrowth("+16%")
                .netCashFlow("₹18.5 L")
                .gstTurnover("₹85 L")
                .revenueTrend(Arrays.asList(
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.RevenueTrendDto.builder().month("Jan").value(140.0).build(),
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.RevenueTrendDto.builder().month("Feb").value(110.0).build(),
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.RevenueTrendDto.builder().month("Mar").value(120.0).build(),
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.RevenueTrendDto.builder().month("Apr").value(70.0).build(),
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.RevenueTrendDto.builder().month("May").value(60.0).build(),
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.RevenueTrendDto.builder().month("Jun").value(30.0).build()
                ))
                .cashFlows(Arrays.asList(
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.CashFlowDto.builder().month("Apr").inflow(80.0).outflow(64.0).build(),
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.CashFlowDto.builder().month("May").inflow(96.0).outflow(56.0).build(),
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.CashFlowDto.builder().month("Jun").inflow(112.0).outflow(72.0).build()
                ))
                .gstTaxableValues(Arrays.asList(
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.GstTaxableValueDto.builder().month("July").taxableValue("₹24.5L").progress(0.85).build(),
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.GstTaxableValueDto.builder().month("August").taxableValue("₹18.2L").progress(0.65).build(),
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.GstTaxableValueDto.builder().month("September").taxableValue("₹32.0L").progress(1.0).build()
                ))
                .digitalAdoptionPercentage(72)
                .costCenters(Arrays.asList(
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.CostCenterDto.builder().name("Inventory").percentage(42).icon("inventory_2").build(),
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.CostCenterDto.builder().name("Salaries").percentage(28).icon("payments").build(),
                        com.hackathon.healthcard.dto.RevenueAnalyticsResponse.CostCenterDto.builder().name("Utilities").percentage(12).icon("bolt").build()
                ))
                .dsoDays(34)
                .dsoTrend("-4 days vs LY")
                .build();
    }
}
