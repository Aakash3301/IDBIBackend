package com.hackathon.healthcard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GSTComplianceInputResDto {
    private int totalReturnsExpected;
    private int totalReturnsFiled;
    private int delayedReturns;
    private double averageDelayDays;
    private double totalTaxExpected;
    private double totalTaxPaid;
    private int latePayments;
    private boolean gstActive;
    private boolean suspended;
    private double eInvoiceCompliancePercentage;
    private double turnoverGrowthPercentage;
    private int gstNoticeCount;
    private int businessAgeMonths;
}
