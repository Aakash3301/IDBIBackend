package com.hackathon.healthcard.entity;

import com.hackathon.healthcard.entity.enums.LoanEligibility;
import com.hackathon.healthcard.entity.enums.RiskCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "financial_health")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialHealth {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "msme_id", nullable = false, unique = true)
    private MSME msme;

    @Column(nullable = false)
    private Integer healthScore; // 0 to 1000

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskCategory riskCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanEligibility loanEligibility;

    private BigDecimal recommendedLoanAmount;

    @com.fasterxml.jackson.annotation.JsonRawValue
    @Column(columnDefinition = "TEXT")
    private String aa;

    @com.fasterxml.jackson.annotation.JsonRawValue
    @Column(columnDefinition = "TEXT")
    private String epfo;

    @UpdateTimestamp
    private LocalDateTime lastCalculatedAt;
}
