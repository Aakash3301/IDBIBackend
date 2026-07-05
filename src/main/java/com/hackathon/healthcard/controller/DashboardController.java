package com.hackathon.healthcard.controller;

import com.hackathon.healthcard.dto.APIResponse;
import com.hackathon.healthcard.dto.DashboardResponseDto;
import com.hackathon.healthcard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<DashboardResponseDto>> getDashboard(@PathVariable UUID id) {
        DashboardResponseDto dashboardData = dashboardService.getDashboardData(id);
        APIResponse<DashboardResponseDto> response = new APIResponse<>(
                HttpStatus.OK.value(),
                "Successfully fetched dashboard data",
                dashboardData
        );
        return ResponseEntity.ok(response);
    }
}
