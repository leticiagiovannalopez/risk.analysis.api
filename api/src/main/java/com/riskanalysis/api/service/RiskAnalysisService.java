package com.riskanalysis.api.service;

import com.riskanalysis.api.model.Person;
import com.riskanalysis.api.model.RiskAnalysis;
import com.riskanalysis.api.repository.RiskAnalysisRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RiskAnalysisService {

    private final RiskAnalysisRepository riskAnalysisRepository;

    public RiskAnalysisService(RiskAnalysisRepository riskAnalysisRepository) {
        this.riskAnalysisRepository = riskAnalysisRepository;
    }

    public RiskAnalysis analyze(Person person, Double score, Double debts) {
        RiskAnalysis analysis = new RiskAnalysis();
        analysis.setPerson(person);
        analysis.setScore(score);
        analysis.setDebts(debts);
        analysis.setAnalysisDate(LocalDateTime.now());

        String riskLevel = calculateRiskLevel(score, debts, person.getMonthlyIncome());
        analysis.setRiskLevel(riskLevel);
        analysis.setJustification(buildJustification(score, debts, person.getMonthlyIncome()));

        return riskAnalysisRepository.save(analysis);
    }

    private String calculateRiskLevel(Double score, Double debts, Double income) {
        double debtRatio = debts / income;

        if (score > 700 && debtRatio < 0.3) return "BAIXO";
        if (score < 500 || debtRatio > 0.6) return "ALTO";
        return "MEDIO";

    }

    private String buildJustification(Double score, Double debts, Double income) {
        double debtRatio = debts / income * 100;
        return String.format("Score: %.0f | Dívida: %.1f%% da renda", score, debtRatio);
    }

    public List<RiskAnalysis> findByPersonId(Long personId) {
        return riskAnalysisRepository.findByPersonId(personId);
    }
}

