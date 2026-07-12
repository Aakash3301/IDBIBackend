package com.hackathon.healthcard.service.engine.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class EPFOScoreEngine {

    public String calculateScore(String msmeId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = Paths.get("src", "main", "java", "com", "hackathon", "healthcard", "util", "mockdata", "EPFO.json").toFile();
            
            if (!jsonFile.exists()) {
                return "{\"error\": \"Mock data file EPFO.json not found\"}";
            }

            JsonNode rootNode = mapper.readTree(jsonFile);
            String fileMsmeId = rootNode.path("msmeId").asText();

            if (!msmeId.equals(fileMsmeId)) {
                return "{\"msmeId\": \"" + msmeId + "\", \"score\": 0, \"reason\": \"No EPFO data found for this MSME\"}";
            }

            // Calculate score
            JsonNode compliance = rootNode.path("complianceIndicators");
            double onTimeFilingRate = compliance.path("onTimeFilingRate").asDouble(0);
            int monthsFiled = compliance.path("monthsFiledLast6").asInt(0);

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
