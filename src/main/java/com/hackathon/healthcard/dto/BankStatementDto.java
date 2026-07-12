package com.hackathon.healthcard.dto;

import lombok.Data;

@Data
public class BankStatementDto {
    private String msmeId;
    private double closingBalance;
    private int transactionCount;
    private double totalDeposits;
    private double totalWithdrawals;
}
