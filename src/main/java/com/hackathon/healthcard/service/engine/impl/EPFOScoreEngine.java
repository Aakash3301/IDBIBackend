package com.hackathon.healthcard.service.engine.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.EPFODto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EPFOScoreEngine {

    public String calculateScore(EPFODto epfoDto) {
        if (epfoDto == null) {
            return "{\"score\": 0, \"reason\": \"No EPFO data found for this MSME\"}";
        }
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            String msmeId = epfoDto.getMsmeId();

            double onTimeFilingRate = epfoDto.getOnTimeFilingRate();
            int monthsFiled = epfoDto.getMonthsFiledLast6();

            int score = 0;
            
            // Rule 1: On-time filing rate (up to 70 points)
            score += (int) (onTimeFilingRate * 0.7);
            
            // Rule 2: Recent filings consistency (up to 30 points)
            if (monthsFiled == 6) {
                score += 30;
            } else if (monthsFiled >= 4) {
                score += 20;
            } else if (monthsFiled >= 2) {
                score += 10;
            }

            score = Math.min(score, 100);

            Map<String, Object> result = new HashMap<>();
            result.put("msmeId", msmeId);
            result.put("engine", "EPFO");
            result.put("score", score);
            result.put("onTimeFilingRate", onTimeFilingRate);
            result.put("monthsFiledLast6", monthsFiled);
            result.put("recommendation", score > 70 ? "Good compliance, favorable for loan" : "Poor compliance, higher risk");

            return mapper.writeValueAsString(result);

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Exception occurred while calculating EPFO score: " + e.getMessage() + "\"}";
        }
    }
}
