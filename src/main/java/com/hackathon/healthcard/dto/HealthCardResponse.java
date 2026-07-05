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
public class HealthCardResponse {
    private String companyName;
    private String profileImageUrl;
    private int overallScore;
    private int maxScore;
    private String statusText;
    private List<BadgeDto> badges;
    private List<ScoreBreakdownDto> scoreBreakdowns;
    private List<String> strengths;
    private List<String> risks;
    private LoanOfferDto loanOffer;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BadgeDto {
        private String text;
        private String icon;
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScoreBreakdownDto {
        private String label;
        private int value;
        private String color;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoanOfferDto {
        private String title;
        private String description;
        private String buttonText;
    }
}
