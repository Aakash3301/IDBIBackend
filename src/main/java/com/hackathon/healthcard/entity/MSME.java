package com.hackathon.healthcard.entity;

import com.hackathon.healthcard.entity.enums.IndustryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "msme")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MSME {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false, unique = true)
    private String pan;

    @Column(nullable = false, unique = true)
    private String gstNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IndustryType industryType;

    @Column(nullable = false, unique = true)
    private String mobileNumber;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
