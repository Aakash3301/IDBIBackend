package com.hackathon.healthcard.service.mock;

import com.hackathon.healthcard.entity.FinancialData;
import com.hackathon.healthcard.entity.MSME;

import java.util.List;

public interface MockDataService {
    List<FinancialData> generateMockDataForMsme(MSME msme);
    void syncGstData(MSME msme);
    void syncUpiData(MSME msme);
    void syncEpfoData(MSME msme);
    void syncBankStatementData(MSME msme);
    void syncAaData(MSME msme);
}
