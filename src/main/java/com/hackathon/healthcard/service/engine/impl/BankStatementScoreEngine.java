package com.hackathon.healthcard.service.engine.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.BankStatementDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BankStatementScoreEngine {

    public String calculateScore(BankStatementDto dto) {
        if (dto == null) {
            return "{\"score\": 0, \"reason\": \"No Bank Statement data found for this MSME\"}";
        }
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            String msmeId = dto.getMsmeId();

            int score = 0;
            double closingBalance = dto.getClosingBalance();
            double deposits = dto.getTotalDeposits();
            double withdrawals = dto.getTotalWithdrawals();
            int txnCount = dto.getTransactionCount();

            // Rule 1: Healthy Closing Balance
            if (closingBalance > 200000) {
                score += 30;
            } else if (closingBalance > 50000) {
                score += 20;
            } else if (closingBalance > 10000) {
                score += 10;
            }

            // Rule 2: Deposit vs Withdrawal
            if (deposits > withdrawals * 1.5) {
                score += 40;
            } else if (deposits > withdrawals) {
                score += 25;
            } else if (deposits > withdrawals * 0.8) {
                score += 10;
            }

            // Rule 3: Transaction Volume
            if (txnCount > 100) {
                score += 30;
            } else if (txnCount > 30) {
                score += 15;
            }

            score = Math.min(score, 100);

            Map<String, Object> result = new HashMap<>();
            result.put("msmeId", msmeId);
            result.put("engine", "BANKSTATEMENT");
            result.put("score", score);
            result.put("closingBalance", closingBalance);
            result.put("totalDeposits", deposits);
            result.put("totalWithdrawals", withdrawals);
            result.put("recommendation", score > 60 ? "Strong bank statement" : "Weak bank statement");

            return mapper.writeValueAsString(result);

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Exception occurred while calculating Bank Statement score: " + e.getMessage() + "\"}";
        }
    }
}
