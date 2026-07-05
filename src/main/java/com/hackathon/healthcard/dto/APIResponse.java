package com.hackathon.healthcard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {
    private int statusCode;
    private String message;
    private T data;
}
