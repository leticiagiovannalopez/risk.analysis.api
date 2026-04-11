package com.riskanalysis.api.repository;

import com.riskanalysis.api.model.RiskAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RiskAnalysisRepository extends JpaRepository<RiskAnalysis, Long> {
    List<RiskAnalysis> findByPersonId(Long personId);
}
