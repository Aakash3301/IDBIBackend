package com.hackathon.healthcard.controller;

import com.hackathon.healthcard.dto.APIResponse;
import com.hackathon.healthcard.entity.FinancialHealth;
import com.hackathon.healthcard.entity.MSME;
import com.hackathon.healthcard.service.MSMEService;
import com.hackathon.healthcard.service.engine.FinancialHealthEngine;
import com.hackathon.healthcard.service.mock.MockDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sync")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class DataSyncController {

    private final MSMEService msmeService;
    private final MockDataService mockDataService;
    private final FinancialHealthEngine financialHealthEngine;

    @PostMapping("/{msmeId}/gst")
    public ResponseEntity<APIResponse<String>> syncGstData(@PathVariable UUID msmeId) {
        MSME msme = msmeService.getMsmeById(msmeId);
        mockDataService.syncGstData(msme);
        return buildSuccessResponse("GST data successfully synced.");
    }

    @PostMapping("/{msmeId}/upi")
    public ResponseEntity<APIResponse<String>> syncUpiData(@PathVariable UUID msmeId) {
        MSME msme = msmeService.getMsmeById(msmeId);
        mockDataService.syncUpiData(msme);
        return buildSuccessResponse("UPI data successfully synced.");
    }

    @PostMapping("/{msmeId}/epfo")
    public ResponseEntity<APIResponse<String>> syncEpfoData(@PathVariable UUID msmeId) {
        MSME msme = msmeService.getMsmeById(msmeId);
        mockDataService.syncEpfoData(msme);
        return buildSuccessResponse("EPFO data successfully synced.");
    }

    @PostMapping("/{msmeId}/bank-statement")
    public ResponseEntity<APIResponse<String>> syncBankStatementData(@PathVariable UUID msmeId) {
        MSME msme = msmeService.getMsmeById(msmeId);
        mockDataService.syncBankStatementData(msme);
        return buildSuccessResponse("Bank statement data successfully synced.");
    }

    @PostMapping("/{msmeId}/aa")
    public ResponseEntity<APIResponse<String>> syncAaData(@PathVariable UUID msmeId) {
        MSME msme = msmeService.getMsmeById(msmeId);
        mockDataService.syncAaData(msme);
        return buildSuccessResponse("Account Aggregator (AA) data successfully synced.");
    }

    @PostMapping("/{msmeId}/calculate-health-score")
    public ResponseEntity<APIResponse<FinancialHealth>> calculateHealthScore(@PathVariable UUID msmeId) {
        MSME msme = msmeService.getMsmeById(msmeId);
        FinancialHealth health = financialHealthEngine.calculateHealthScore(msme);
        APIResponse<FinancialHealth> response = new APIResponse<>(
                HttpStatus.OK.value(),
                "Successfully generated Health Score from the synced data.",
                health
        );
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<APIResponse<String>> buildSuccessResponse(String message) {
        APIResponse<String> response = new APIResponse<>(
                HttpStatus.OK.value(),
                message,
                null
        );
        return ResponseEntity.ok(response);
    }
}
