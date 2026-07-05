package com.hackathon.healthcard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Mobile number is required for login")
    private String mobileNumber;
}
