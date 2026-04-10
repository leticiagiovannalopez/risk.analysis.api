package com.riskanalysis.api.model;
import lombok.Data;

@Data
public class Person {
    private Long id;
    private String name;
    private String cpf;
    private Double monthlyIncome;
}

