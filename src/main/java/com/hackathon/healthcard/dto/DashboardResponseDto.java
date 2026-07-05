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
public class DashboardResponseDto {
    private String companyName;
    private String sector;
    private boolean hasNotifications;
    private String profileImageUrl;
    private HealthScoreDto healthScore;
    private List<KpiDto> kpis;
    private LoanEligibilityDto loanEligibility;
    private List<QuickActionDto> quickActions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthScoreDto {
        private int score;
        private int maxScore;
        private String statusText;
        private List<String> tags;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KpiDto {
        private String id;
        private String icon;
        private String title;
        private String value;
        private String trend;
        private String trendDirection;
        private String colorType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoanEligibilityDto {
        private String status;
        private String amountText;
        private int confidencePercentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuickActionDto {
        private String id;
        private String icon;
        private String title;
    }
}
