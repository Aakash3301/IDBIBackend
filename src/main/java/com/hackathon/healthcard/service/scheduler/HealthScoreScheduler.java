package com.hackathon.healthcard.service.scheduler;

import com.hackathon.healthcard.entity.MSME;
import com.hackathon.healthcard.repository.MSMERepository;
import com.hackathon.healthcard.service.engine.FinancialHealthEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthScoreScheduler {

    private final MSMERepository msmeRepository;
    private final FinancialHealthEngine financialHealthEngine;

    /**
     * This scheduled task runs every 24 hours (86,400,000 milliseconds).
     * It recalculates the health score for every MSME in the database.
     */
    @Scheduled(fixedRate = 86400000)
    public void recalculateAllHealthScores() {
        log.info("Starting scheduled 24-hour health score recalculation task...");
        List<MSME> msmes = msmeRepository.findAll();
        
        int successCount = 0;
        int failCount = 0;
        
        for (MSME msme : msmes) {
            try {
                financialHealthEngine.calculateHealthScore(msme);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to calculate health score for MSME ID: {}", msme.getId(), e);
                failCount++;
            }
        }
        
        log.info("Finished health score recalculation. Successfully updated: {}, Failed: {}", successCount, failCount);
    }
}
