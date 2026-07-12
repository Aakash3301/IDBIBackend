package com.hackathon.healthcard.util;

import com.hackathon.healthcard.dto.GSTComplianceInputResDto;

public class GSTComplianceScoreCalculator {

    public int calculate(GSTComplianceInputResDto input) {

        if (!input.isGstActive() || input.isSuspended()) {
            return 0;
        }

        double score = 100.0;

        // Filing Compliance (30 Marks)
        double filingRate = (double) input.getTotalReturnsFiled() / input.getTotalReturnsExpected();
        score -= (1 - filingRate) * 30;

        // Delay Penalty (20 Marks)
        score -= Math.min(input.getDelayedReturns() * 2.0, 20.0);

        // Average Delay Days (10 Marks)
        score -= Math.min(input.getAverageDelayDays() * 0.5, 10.0);

        // GST Payment Compliance (20 Marks)
        double paymentRate = input.getTotalTaxPaid() / input.getTotalTaxExpected();
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

        // Clamp score between 0 and 100
        score = Math.max(0.0, Math.min(score, 100.0));

        return (int) score;
    }
}
