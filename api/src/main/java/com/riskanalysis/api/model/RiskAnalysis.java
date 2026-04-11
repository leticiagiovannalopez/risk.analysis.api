package com.riskanalysis.api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "risk_analysis")
public class RiskAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double score;
    private Double debts;
    private String riskLevel;
    private String justification;
    private LocalDateTime analysisDate;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
