package com.hackathon.healthcard.service.engine.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.healthcard.dto.GstScoreResult;
import com.hackathon.healthcard.entity.FinancialData;
import com.hackathon.healthcard.entity.FinancialHealth;
import com.hackathon.healthcard.entity.MSME;
import com.hackathon.healthcard.entity.enums.LoanEligibility;
import com.hackathon.healthcard.entity.enums.RiskCategory;
import com.hackathon.healthcard.repository.FinancialDataRepository;
import com.hackathon.healthcard.repository.FinancialHealthRepository;
import com.hackathon.healthcard.service.engine.FinancialHealthEngine;
import com.hackathon.healthcard.service.reader.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialHealthEngineImpl implements FinancialHealthEngine {

    private final FinancialDataRepository financialDataRepository;
    private final FinancialHealthRepository financialHealthRepository;
    
    private final AAMockDataReader aaReader;
    private final EPFOMockDataReader epfoReader;
    private final UPIMockDataReader upiReader;
    private final BankStatementMockDataReader bankStatementReader;
    private final GSTMockDataReader gstReader;

    private final AAScoreEngine aaScoreEngine;
    private final EPFOScoreEngine epfoScoreEngine;
    private final UPIScoreEngine upiScoreEngine;
    private final BankStatementScoreEngine bankStatementScoreEngine;
    private final GSTComplianceScoreCalculator gstComplianceScoreCalculator;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public FinancialHealth calculateHealthScore(MSME msme) {

        List<FinancialData> history = financialDataRepository.findByMsmeIdOrderByRecordMonthAsc(msme.getId());
        
        BigDecimal avgMonthlyRevenue = BigDecimal.ZERO;
        
        if (!history.isEmpty()) {
            BigDecimal totalRevenue = BigDecimal.ZERO;
            for (FinancialData data : history) {
                totalRevenue = totalRevenue.add(data.getGstRevenue());
            }
            avgMonthlyRevenue = totalRevenue.divide(BigDecimal.valueOf(history.size()), 2, RoundingMode.HALF_UP);
        } else {
            // fallback to some default if history not found
            avgMonthlyRevenue = BigDecimal.valueOf(500000);
        }

        String msmeIdStr = msme.getId().toString();

        String aaResult = aaScoreEngine.calculateScore(aaReader.readData(msmeIdStr));
        String epfoResult = epfoScoreEngine.calculateScore(epfoReader.readData(msmeIdStr));
        String upiResult = upiScoreEngine.calculateScore(upiReader.readData(msmeIdStr));
        String bankStatementResult = bankStatementScoreEngine.calculateScore(bankStatementReader.readData(msmeIdStr));
        GstScoreResult gstResult = gstComplianceScoreCalculator.calculate(gstReader.readData(msmeIdStr));

        int aaScore = extractScore(aaResult);
        int epfoScore = extractScore(epfoResult);
        int upiScore = extractScore(upiResult);
        int bankStatementScore = extractScore(bankStatementResult);
        int gstScore = gstResult.score();

        int averageScore = (aaScore + epfoScore + upiScore + bankStatementScore + gstScore) / 5;
        
        // Score is already 0-100 average
        int score = averageScore;

        RiskCategory riskCategory;
        if (score >= 80) riskCategory = RiskCategory.LOW;
        else if (score >= 60) riskCategory = RiskCategory.MEDIUM;
        else riskCategory = RiskCategory.HIGH;

        LoanEligibility loanEligibility;
        BigDecimal recommendedLoanAmount = BigDecimal.ZERO;

        if (riskCategory == RiskCategory.LOW) {
            loanEligibility = LoanEligibility.ELIGIBLE;
            recommendedLoanAmount = avgMonthlyRevenue.multiply(BigDecimal.valueOf(3));
        } else if (riskCategory == RiskCategory.MEDIUM) {
            loanEligibility = LoanEligibility.REVIEW;
            recommendedLoanAmount = avgMonthlyRevenue;
        } else {
            loanEligibility = LoanEligibility.REJECTED;
        }

        FinancialHealth healthProfile = financialHealthRepository.findByMsmeId(msme.getId())
                .orElse(new FinancialHealth());

        healthProfile.setMsme(msme);
        healthProfile.setHealthScore(score);
        healthProfile.setRiskCategory(riskCategory);
        healthProfile.setLoanEligibility(loanEligibility);
        healthProfile.setRecommendedLoanAmount(recommendedLoanAmount);
        
        healthProfile.setAa(aaResult);
        healthProfile.setEpfo(epfoResult);
        healthProfile.setUpi(upiResult);
        healthProfile.setBankStatement(bankStatementResult);
        
        try {
            java.util.Map<String, Object> gstMap = new java.util.HashMap<>();
            gstMap.put("engine", "GST");
            gstMap.put("score", gstResult.score());
            gstMap.put("grade", gstResult.grade());
            gstMap.put("strengths", gstResult.strengths());
            gstMap.put("weaknesses", gstResult.weaknesses());
            gstMap.put("recommendations", gstResult.recommendations());
            healthProfile.setGst(objectMapper.writeValueAsString(gstMap));
        } catch (Exception e) {
            healthProfile.setGst("{\"engine\":\"GST\",\"error\":\"Failed to serialize\"}");
        }

        return financialHealthRepository.save(healthProfile);
    }

    private int extractScore(String jsonResult) {
        try {
            if (jsonResult == null || jsonResult.isEmpty()) return 0;
            JsonNode node = objectMapper.readTree(jsonResult);
            if (node.has("score")) {
                return node.get("score").asInt(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
