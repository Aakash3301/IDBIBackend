package com.hackathon.healthcard.service.mock.impl;

import com.hackathon.healthcard.entity.FinancialData;
import com.hackathon.healthcard.entity.MSME;
import com.hackathon.healthcard.repository.FinancialDataRepository;
import com.hackathon.healthcard.service.mock.MockDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MockDataServiceImpl implements MockDataService {

    private final FinancialDataRepository financialDataRepository;
    private final Random random = new Random();

    @Override
    @Transactional
    public List<FinancialData> generateMockDataForMsme(MSME msme) {
        List<FinancialData> generatedData = new ArrayList<>();
        YearMonth currentMonth = YearMonth.now();

        double baseRevenue = switch (msme.getIndustryType()) {
            case RETAIL -> 500_000 + random.nextInt(1_000_000);
            case MANUFACTURING -> 2_000_000 + random.nextInt(3_000_000);
            case SERVICES -> 1_000_000 + random.nextInt(1_500_000);
        };

        for (int i = 5; i >= 0; i--) {
            YearMonth recordMonth = currentMonth.minusMonths(i);
            
            double variance = 0.90 + (random.nextDouble() * 0.25);
            double monthlyRevenue = baseRevenue * variance;

            double upiPercentage = switch (msme.getIndustryType()) {
                case RETAIL -> 0.60;
                case SERVICES -> 0.40;
                case MANUFACTURING -> 0.10;
            };
            
            double upiValue = monthlyRevenue * upiPercentage;
            int upiVolume = (int) (upiValue / (500 + random.nextInt(1000))); 

            int employeeCount = switch (msme.getIndustryType()) {
                case RETAIL -> 5 + random.nextInt(3);
                case MANUFACTURING -> 50 + random.nextInt(10);
                case SERVICES -> 15 + random.nextInt(5);
            };

            double bankBalance = monthlyRevenue * 0.4; 

            FinancialData data = FinancialData.builder()
                    .msme(msme)
                    .recordMonth(recordMonth.toString())
                    .gstRevenue(BigDecimal.valueOf(monthlyRevenue).setScale(2, RoundingMode.HALF_UP))
                    .upiValue(BigDecimal.valueOf(upiValue).setScale(2, RoundingMode.HALF_UP))
                    .upiVolume(upiVolume)
                    .epfoEmployeeCount(employeeCount)
                    .avgBankBalance(BigDecimal.valueOf(bankBalance).setScale(2, RoundingMode.HALF_UP))
                    .build();

            generatedData.add(data);
        }

        return financialDataRepository.saveAll(generatedData);
    }
}
