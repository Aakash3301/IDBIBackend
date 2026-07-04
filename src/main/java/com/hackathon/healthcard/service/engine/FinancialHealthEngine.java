package com.hackathon.healthcard.service.engine;

import com.hackathon.healthcard.entity.FinancialHealth;
import com.hackathon.healthcard.entity.MSME;

public interface FinancialHealthEngine {
    FinancialHealth calculateHealthScore(MSME msme);
}
