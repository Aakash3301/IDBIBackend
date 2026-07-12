package com.hackathon.healthcard.service.engine.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class AAScoreEngine {

    public String calculateScore(String msmeId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = Paths.get("src", "main", "java", "com", "hackathon", "healthcard", "util", "mockdata", "AA.json").toFile();
            
            if (!jsonFile.exists()) {
                return "{\"error\": \"Mock data file AA.json not found\"}";
            }

            JsonNode rootNode = mapper.readTree(jsonFile);
            String fileMsmeId = rootNode.path("msmeId").asText();

            if (!msmeId.equals(fileMsmeId)) {
                return "{\"msmeId\": \"" + msmeId + "\", \"score\": 0, \"reason\": \"No AA data found for this MSME\"}";
            }

            // Calculate score
            JsonNode summary = rootNode.path("Account").path("Summary");
            double currentBalance = summary.path("currentBalance").asDouble(0);

            JsonNode transactions = rootNode.path("Account").path("Transactions").path("Transaction");
            double totalCredit = 0;
            double totalDebit = 0;

            if (transactions.isArray()) {
                for (JsonNode txn : transactions) {
                    String type = txn.path("type").asText();
                    double amount = txn.path("amount").asDouble(0);
                    if ("CREDIT".equalsIgnoreCase(type)) {
                        totalCredit += amount;
                    } else if ("DEBIT".equalsIgnoreCase(type)) {
                        totalDebit += amount;
                    }
                }
            }

            int score = 0;
            
            // Rule 1: Balance Check (up to 30 points)
            if (currentBalance > 100000) {
                score += 30;
            } else if (currentBalance > 50000) {
                score += 20;
            } else if (currentBalance > 10000) {
                score += 10;
            }

            // Rule 2: Cash flow Check (up to 40 points)
            if (totalCredit > totalDebit * 1.2) {
                score += 40;
            } else if (totalCredit > totalDebit) {
                score += 30;
            } else if (totalCredit > totalDebit * 0.8) {
                score += 15;
            }

            // Rule 3: Transaction volume (up to 30 points)
            if (transactions.size() > 20) {
                score += 30;
            } else if (transactions.size() > 10) {
                score += 20;
            } else if (transactions.size() > 0) {
                score += 10;
            }

            score = Math.min(score, 100);

            Map<String, Object> result = new HashMap<>();
            result.put("msmeId", msmeId);
            result.put("engine", "AA");
            result.put("score", score);
            result.put("currentBalance", currentBalance);
            result.put("totalCredit", totalCredit);
            result.put("totalDebit", totalDebit);
            result.put("recommendation", score > 60 ? "Favorable for loan based on bank statement" : "High risk based on bank statement");

            return mapper.writeValueAsString(result);

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Exception occurred while calculating AA score: " + e.getMessage() + "\"}";
        }
    }
}
