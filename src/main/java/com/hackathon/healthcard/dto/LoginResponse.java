package com.hackathon.healthcard.dto;

import com.hackathon.healthcard.entity.MSME;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean isValid;
    private TokenDto token;
    private MSME msme;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenDto {
        private String accessToken;
        private String refreshToken;
        private int expiresIn;
    }
}
