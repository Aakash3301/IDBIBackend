package com.hackathon.healthcard.service.engine.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.UPIDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UPIScoreEngine {

    public String calculateScore(UPIDto upiDto) {
        if (upiDto == null) {
            return "{\"score\": 0, \"reason\": \"No UPI data found for this MSME\"}";
        }
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            String msmeId = upiDto.getMsmeId();

            double totalCredit = 0;
            double totalDebit = 0;

            if (upiDto.getTransactions() != null) {
                for (UPIDto.Transaction txn : upiDto.getTransactions()) {
                    if ("CREDIT".equalsIgnoreCase(txn.getType())) {
                        totalCredit += txn.getAmount();
                    } else if ("DEBIT".equalsIgnoreCase(txn.getType())) {
                        totalDebit += txn.getAmount();
                    }
                }
            }

            int txnCount = upiDto.getTransactionCount();
            int score = 0;

            // Rule 1: Credit > Debit
            if (totalCredit > totalDebit * 1.5) {
                score += 50;
            } else if (totalCredit > totalDebit * 1.1) {
                score += 30;
            } else if (totalCredit > totalDebit * 0.8) {
                score += 10;
            }

            // Rule 2: Transaction Volume
            if (txnCount > 150) {
                score += 50;
            } else if (txnCount > 50) {
                score += 30;
            } else if (txnCount > 10) {
                score += 10;
            }

            score = Math.min(score, 100);

            Map<String, Object> result = new HashMap<>();
            result.put("msmeId", msmeId);
            result.put("engine", "UPI");
            result.put("score", score);
            result.put("totalCredit", totalCredit);
            result.put("totalDebit", totalDebit);
            result.put("transactionCount", txnCount);
            result.put("recommendation", score > 60 ? "Good UPI transaction volume" : "Low UPI usage");

            return mapper.writeValueAsString(result);

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Exception occurred while calculating UPI score: " + e.getMessage() + "\"}";
        }
    }
}
