package com.hackathon.healthcard.dto;

import lombok.Data;
import java.util.List;

@Data
public class AADto {
    private String msmeId;
    private double currentBalance;
    private List<Transaction> transactions;

    @Data
    public static class Transaction {
        private String type;
        private double amount;
    }
}
