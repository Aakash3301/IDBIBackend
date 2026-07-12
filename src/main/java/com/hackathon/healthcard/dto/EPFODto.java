package com.hackathon.healthcard.dto;

import lombok.Data;

@Data
public class EPFODto {
    private String msmeId;
    private double onTimeFilingRate;
    private int monthsFiledLast6;
}
