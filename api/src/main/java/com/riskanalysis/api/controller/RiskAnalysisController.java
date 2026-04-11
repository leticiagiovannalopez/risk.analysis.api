package com.riskanalysis.api.controller;

import com.riskanalysis.api.model.RiskAnalysis;
import com.riskanalysis.api.service.PersonService;
import com.riskanalysis.api.service.RiskAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/risk")
public class RiskAnalysisController {

    private final RiskAnalysisService riskAnalysisService;
    private final PersonService personService;

    public RiskAnalysisController(RiskAnalysisService riskAnalysisService, PersonService personService) {
        this.riskAnalysisService = riskAnalysisService;
        this.personService = personService;
    }

    @PostMapping("/analyze/{personId}")
    public ResponseEntity<RiskAnalysis> analyze(
            @PathVariable Long personId,
            @RequestParam Double score,
            @RequestParam Double debts) {
        var person = personService.findById(personId);
        return ResponseEntity.ok(riskAnalysisService.analyze(person, score, debts));
    }

    @GetMapping("/history/{personId}")
    public ResponseEntity<List<RiskAnalysis>> history(@PathVariable Long personId) {
        return ResponseEntity.ok(riskAnalysisService.findByPersonId(personId));
    }

}