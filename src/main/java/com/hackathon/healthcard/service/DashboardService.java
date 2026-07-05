package com.hackathon.healthcard.service;

import com.hackathon.healthcard.dto.DashboardResponseDto;
import com.hackathon.healthcard.entity.MSME;
import com.hackathon.healthcard.util.MockDataConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MSMEService msmeService;

    public DashboardResponseDto getDashboardData(UUID msmeId) {
        // Fetch the actual MSME details to make the dashboard somewhat dynamic
        MSME msme = msmeService.getMsmeById(msmeId);

        // Fetch mock data from the centralized static utility
        return MockDataConstants.generateDashboardMockData(msme);
    }
}
