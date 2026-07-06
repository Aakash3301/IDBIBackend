package com.hackathon.healthcard.controller;

import com.hackathon.healthcard.dto.*;
import com.hackathon.healthcard.entity.FinancialHealth;
import com.hackathon.healthcard.entity.MSME;
import com.hackathon.healthcard.service.MSMEService;
import com.hackathon.healthcard.service.engine.FinancialHealthEngine;
import com.hackathon.healthcard.service.mock.MockDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/msmes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allows the frontend to connect locally without CORS issues
@Slf4j
public class MSMEController {

    private final MSMEService msmeService;
    private final MockDataService mockDataService;
    private final FinancialHealthEngine financialHealthEngine;



    @GetMapping("/{id}/health-card")
    public ResponseEntity<APIResponse<com.hackathon.healthcard.dto.HealthCardResponse>> getHealthCard(@PathVariable UUID id) {
        com.hackathon.healthcard.dto.HealthCardResponse healthCard = msmeService.getHealthCard(id);
        APIResponse<com.hackathon.healthcard.dto.HealthCardResponse> response = new APIResponse<>(
                HttpStatus.OK.value(),
                "Health card retrieved successfully",
                healthCard
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/revenue-analytics")
    public ResponseEntity<APIResponse<com.hackathon.healthcard.dto.RevenueAnalyticsResponse>> getRevenueAnalytics(@PathVariable UUID id) {
        com.hackathon.healthcard.dto.RevenueAnalyticsResponse analytics = msmeService.getRevenueAnalytics(id);
        APIResponse<com.hackathon.healthcard.dto.RevenueAnalyticsResponse> response = new APIResponse<>(
                HttpStatus.OK.value(),
                "API successfully",
                analytics
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/loanAssessment")
    public ResponseEntity<APIResponse<com.hackathon.healthcard.dto.LoanAssessmentResponse>> assessLoan(@Valid @RequestBody com.hackathon.healthcard.dto.LoanAssessmentRequest request) {
        com.hackathon.healthcard.dto.LoanAssessmentResponse assessment = msmeService.assessLoan(request);
        APIResponse<com.hackathon.healthcard.dto.LoanAssessmentResponse> response = new APIResponse<>(
                HttpStatus.OK.value(),
                "Loan assessment completed successfully",
                assessment
        );
        return ResponseEntity.ok(response);
    }
}
