package com.hackathon.healthcard.dto;

import java.util.List;

public record GstScoreResult(
        int score,
        String grade,
        List<String> strengths,
        List<String> weaknesses,
        List<String> recommendations
) {
}
