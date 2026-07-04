package com.hackathon.healthcard.service.mock;

import com.hackathon.healthcard.entity.FinancialData;
import com.hackathon.healthcard.entity.MSME;

import java.util.List;

public interface MockDataService {
    List<FinancialData> generateMockDataForMsme(MSME msme);
}
