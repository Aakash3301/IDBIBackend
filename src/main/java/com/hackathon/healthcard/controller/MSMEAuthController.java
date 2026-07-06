package com.hackathon.healthcard.controller;

import com.hackathon.healthcard.dto.APIResponse;
import com.hackathon.healthcard.dto.LoginRequest;
import com.hackathon.healthcard.dto.LoginResponse;
import com.hackathon.healthcard.dto.MsmeCreateRequest;
import com.hackathon.healthcard.entity.MSME;
import com.hackathon.healthcard.service.MSMEService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/msmes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allows the frontend to connect locally without CORS issues
@Slf4j
public class MSMEAuthController {

    private final MSMEService msmeService;

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
                
        APIResponse<LoginResponse> response = new APIResponse<>(
                HttpStatus.OK.value(),
                "Login successful",
                loginResponse
        );
        return ResponseEntity.ok(response);
    }
}
