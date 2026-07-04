package com.hackathon.healthcard.dto;

import com.hackathon.healthcard.entity.enums.IndustryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MsmeCreateRequest {

    @NotBlank(message = "Business Name is required")
    private String businessName;

    @NotBlank(message = "PAN is required")
    private String pan;

    @NotBlank(message = "GST Number is required")
    private String gstNumber;

    @NotNull(message = "Industry Type is required")
    private IndustryType industryType;
}
