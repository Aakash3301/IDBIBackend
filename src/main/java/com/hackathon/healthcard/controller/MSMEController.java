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

    @PostMapping
    public ResponseEntity<APIResponse<MSME>> createMsme(@Valid @RequestBody MsmeCreateRequest request) {
        MSME createdMsme = msmeService.createMsme(request);
        APIResponse<MSME> response = new APIResponse<>(
                HttpStatus.CREATED.value(),
                "MSME created successfully",
                createdMsme
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {



        MSME msme = msmeService.login(request.getMobileNumber());
        
         LoginResponse loginResponse = LoginResponse.builder()
                .isValid(true)
                .token(LoginResponse.TokenDto.builder()
                        .accessToken("mock_access_token_123")
                        .refreshToken("mock_refresh_token_456")
                        .expiresIn(3600)
                        .build())
                .msme(msme)
                .build();
                
        APIResponse<com.hackathon.healthcard.dto.LoginResponse> response = new APIResponse<>(
                HttpStatus.OK.value(),
                "Login successful",
                loginResponse
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/mock-data")
    public ResponseEntity<APIResponse<String>> generateMockData(@PathVariable UUID id) {
        MSME msme = msmeService.getMsmeById(id);
        mockDataService.generateMockDataForMsme(msme);
        APIResponse<String> response = new APIResponse<>(
                HttpStatus.OK.value(),
                "Successfully generated 6 months of historical mock data.",
                null
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/calculate-score")
    public ResponseEntity<APIResponse<FinancialHealth>> calculateScore(@PathVariable UUID id) {
        MSME msme = msmeService.getMsmeById(id);
        FinancialHealth health = financialHealthEngine.calculateHealthScore(msme);
        APIResponse<FinancialHealth> response = new APIResponse<>(
                HttpStatus.OK.value(),
                "Successfully calculated health score.",
                health
        );
        return ResponseEntity.ok(response);
    }

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
}
