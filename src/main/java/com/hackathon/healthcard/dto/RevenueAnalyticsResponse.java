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
public class RevenueAnalyticsResponse {
    private List<String> aiInsights;
    private String totalRevenue;
    private String revenueGrowth;
    private String netCashFlow;
    private String gstTurnover;
    private List<RevenueTrendDto> revenueTrend;
    private List<CashFlowDto> cashFlows;
    private List<GstTaxableValueDto> gstTaxableValues;
    private int digitalAdoptionPercentage;
    private List<CostCenterDto> costCenters;
    private int dsoDays;
    private String dsoTrend;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueTrendDto {
        private String month;
        private double value;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CashFlowDto {
        private String month;
        private double inflow;
        private double outflow;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GstTaxableValueDto {
        private String month;
        private String taxableValue;
        private double progress;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CostCenterDto {
        private String name;
        private int percentage;
        private String icon;
    }
}
