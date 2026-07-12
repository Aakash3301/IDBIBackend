package com.hackathon.healthcard.enums;

import lombok.Getter;

@Getter
public enum GSTComplianceGrade {
    EXCELLENT("Excellent GST Compliance", 90, 100),
    GOOD("Good Compliance", 75, 89),
    AVERAGE("Average Compliance", 60, 74),
    POOR("Poor Compliance", 40, 59),
    HIGH_RISK("High Compliance Risk", 0, 39);

    private final String meaning;
    private final int minScore;
    private final int maxScore;

    GSTComplianceGrade(String meaning, int minScore, int maxScore) {
        this.meaning = meaning;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    public static GSTComplianceGrade fromScore(int score) {
        for (GSTComplianceGrade grade : values()) {
            if (score >= grade.getMinScore() && score <= grade.getMaxScore()) {
                return grade;
            }
        }
        return HIGH_RISK; // Default fallback
    }
}
