package com.hackathon.healthcard.service.engine.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.AADto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AAScoreEngine {

    public String calculateScore(AADto aaDto) {
        if (aaDto == null) {
            return "{\"score\": 0, \"reason\": \"No AA data found for this MSME\"}";
        }
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            String msmeId = aaDto.getMsmeId();

            double currentBalance = aaDto.getCurrentBalance();
            double totalCredit = 0;
            double totalDebit = 0;

            if (aaDto.getTransactions() != null) {
                for (AADto.Transaction txn : aaDto.getTransactions()) {
                    if ("CREDIT".equalsIgnoreCase(txn.getType())) {
                        totalCredit += txn.getAmount();
                    } else if ("DEBIT".equalsIgnoreCase(txn.getType())) {
                        totalDebit += txn.getAmount();
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
            int txnCount = aaDto.getTransactions() != null ? aaDto.getTransactions().size() : 0;
            if (txnCount > 20) {
                score += 30;
            } else if (txnCount > 10) {
                score += 20;
            } else if (txnCount > 0) {
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
