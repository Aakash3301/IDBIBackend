package com.hackathon.healthcard.controller;

import com.hackathon.healthcard.dto.HealthCardResponse;
import com.hackathon.healthcard.dto.MsmeCreateRequest;
import com.hackathon.healthcard.entity.FinancialHealth;
import com.hackathon.healthcard.entity.MSME;
import com.hackathon.healthcard.service.MSMEService;
import com.hackathon.healthcard.service.engine.FinancialHealthEngine;
import com.hackathon.healthcard.service.mock.MockDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/msmes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allows the frontend to connect locally without CORS issues
public class MSMEController {

    private final MSMEService msmeService;
    private final MockDataService mockDataService;
    private final FinancialHealthEngine financialHealthEngine;

    @PostMapping
    public ResponseEntity<MSME> createMsme(@Valid @RequestBody MsmeCreateRequest request) {
        return new ResponseEntity<>(msmeService.createMsme(request), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/mock-data")
    public ResponseEntity<String> generateMockData(@PathVariable UUID id) {
        MSME msme = msmeService.getMsmeById(id);
        mockDataService.generateMockDataForMsme(msme);
        return ResponseEntity.ok("Successfully generated 6 months of historical mock data.");
    }

    @PostMapping("/{id}/calculate-score")
    public ResponseEntity<FinancialHealth> calculateScore(@PathVariable UUID id) {
        MSME msme = msmeService.getMsmeById(id);
        FinancialHealth health = financialHealthEngine.calculateHealthScore(msme);
        return ResponseEntity.ok(health);
    }

    @GetMapping("/{id}/health-card")
    public ResponseEntity<HealthCardResponse> getHealthCard(@PathVariable UUID id) {
        return ResponseEntity.ok(msmeService.getHealthCard(id));
    }
}
