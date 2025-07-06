package com.hburak_dev.psk_full_stack.scoring;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v2/admin/scoring")
@RequiredArgsConstructor
@Tag(name = "Scoring Admin", description = "Admin endpoints for managing scoring strategies")
public class ScoringController {

    private final ScoreCalculationService scoreCalculationService;

    @GetMapping("/strategies")
    @Operation(summary = "Get all available scoring strategies")
    public ResponseEntity<List<ScoringStrategyResponse>> getAllScoringStrategies() {
        List<ScoringStrategyResponse> strategies = new java.util.ArrayList<>();
        
        for (ScoringStrategyType strategyType : ScoringStrategyType.values()) {
            ScoringStrategyResponse response = ScoringStrategyResponse.builder()
                    .name(strategyType.name())
                    .strategyName(strategyType.getStrategyName())
                    .description(strategyType.getDescription())
                    .build();
            strategies.add(response);
        }
        
        return ResponseEntity.ok(strategies);
    }
}