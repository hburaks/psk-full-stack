package com.hburak_dev.psk_full_stack.scoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoringStrategyResponse {
    
    private String name; // Enum name (SIMPLE_LINEAR, WEIGHTED, etc.)
    private String strategyName; // Strategy implementation name
    private String description;
}