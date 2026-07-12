package com.hackathon.healthcard.service.engine.impl;

import com.hackathon.healthcard.dto.GSTComplianceInputResDto;
import com.hackathon.healthcard.dto.GstScoreResult;
import com.hackathon.healthcard.enums.GSTComplianceGrade;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GSTComplianceScoreCalculator {

    public GstScoreResult calculate(GSTComplianceInputResDto input) {
        if (!input.isGstActive() || input.isSuspended()) {
            return generateInactiveResult(input);
        }

        int score = calculateScore(input);
        GSTComplianceGrade grade = GSTComplianceGrade.fromScore(score);
        
        List<String> strengths = new ArrayList<>();
        List<String> weaknesses = new ArrayList<>();
        List<String> recommendations = new ArrayList<>();
        
        generateInsights(input, strengths, weaknesses, recommendations);
        
        return new GstScoreResult(
                score,
                grade.getMeaning(),
                strengths,
                weaknesses,
                recommendations
        );
    }

    private int calculateScore(GSTComplianceInputResDto input) {
        double score = 100.0;

        // Filing Compliance (30 Marks)
        double filingRate = input.getTotalReturnsExpected() > 0 ? 
                (double) input.getTotalReturnsFiled() / input.getTotalReturnsExpected() : 1.0;
        score -= (1 - filingRate) * 30;

        // Delay Penalty (20 Marks)
        score -= Math.min(input.getDelayedReturns() * 2.0, 20.0);

        // Average Delay Days (10 Marks)
        score -= Math.min(input.getAverageDelayDays() * 0.5, 10.0);

        // GST Payment Compliance (20 Marks)
        double paymentRate = input.getTotalTaxExpected() > 0 ? 
                input.getTotalTaxPaid() / input.getTotalTaxExpected() : 1.0;
        score -= (1 - paymentRate) * 20;

        // Late Payment Penalty (5 Marks)
        score -= Math.min(input.getLatePayments(), 5);

        // GST Notice Penalty (10 Marks)
        score -= Math.min(input.getGstNoticeCount() * 2.0, 10.0);

        // E-Invoice Bonus (3 Marks)
        score += (input.getEInvoiceCompliancePercentage() / 100.0) * 3;

        // Growth Bonus (2 Marks)
        if (input.getTurnoverGrowthPercentage() > 15) {
            score += 2;
        }

        return (int) Math.max(0.0, Math.min(score, 100.0));
    }

    private void generateInsights(
            GSTComplianceInputResDto input, 
            List<String> strengths, 
            List<String> weaknesses, 
            List<String> recommendations) {
            
        // Filing Compliance
        if (input.getTotalReturnsFiled() == input.getTotalReturnsExpected() && input.getTotalReturnsExpected() > 0) {
            strengths.add("100% GST Returns Filed");
        } else if (input.getTotalReturnsExpected() > input.getTotalReturnsFiled()) {
            weaknesses.add("Missed GST Return Filings");
            recommendations.add("Ensure all pending GST returns are filed immediately to avoid further penalties.");
        }

        // Delay insights
        if (input.getDelayedReturns() == 0) {
            strengths.add("No delayed returns");
        } else {
            weaknesses.add(input.getDelayedReturns() + " Delayed Return(s)");
            recommendations.add("File GST returns before the due date to avoid late fees.");
        }

        // Tax payment
        if (input.getTotalTaxPaid() >= input.getTotalTaxExpected() && input.getTotalTaxExpected() > 0) {
            strengths.add("No Outstanding Tax");
        } else if (input.getTotalTaxPaid() < input.getTotalTaxExpected()) {
            weaknesses.add("Shortfall in GST Tax Payment");
            recommendations.add("Pay outstanding tax liabilities to avoid interest and notices.");
        }

        // Active Status
        if (input.isGstActive() && !input.isSuspended()) {
            strengths.add("Active GST Registration");
        }

        // Notices
        if (input.getGstNoticeCount() == 0) {
            strengths.add("No GST Notices received");
        } else {
            weaknesses.add("Received " + input.getGstNoticeCount() + " GST Notice(s)");
            recommendations.add("Resolve outstanding GST notices promptly.");
        }
        
        // E-Invoice
        if (input.getEInvoiceCompliancePercentage() > 90) {
            strengths.add("Excellent E-Invoice Compliance");
        }
        
        // General recommendation if everything is good
        if (recommendations.isEmpty()) {
            recommendations.add("Continue timely GST filing and payments to maintain excellent compliance.");
        }
    }

    private GstScoreResult generateInactiveResult(GSTComplianceInputResDto input) {
        return new GstScoreResult(
                0,
                GSTComplianceGrade.HIGH_RISK.getMeaning(),
                List.of(),
                List.of(input.isSuspended() ? "GST Registration is Suspended" : "GST Registration is Inactive"),
                List.of("Immediately contact the GST portal or a tax professional to activate your GST registration.")
        );
    }
}
