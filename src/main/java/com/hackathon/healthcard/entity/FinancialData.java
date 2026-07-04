package com.hackathon.healthcard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "financial_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "msme_id", nullable = false)
    private MSME msme;

    // We use String for simplicity in the hackathon, e.g. "2023-05"
    @Column(nullable = false)
    private String recordMonth; 

    private BigDecimal gstRevenue;
    
    private Integer upiVolume;
    
    private BigDecimal upiValue;
    
    private Integer epfoEmployeeCount;
    
    private BigDecimal avgBankBalance;
}
