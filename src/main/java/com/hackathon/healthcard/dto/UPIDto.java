package com.hackathon.healthcard.dto;

import lombok.Data;
import java.util.List;

@Data
public class UPIDto {
    private String msmeId;
    private int transactionCount;
    private List<Transaction> transactions;

    @Data
    public static class Transaction {
        private String type;
        private double amount;
    }
}
