package com.hburak_dev.psk_full_stack.scoring;

import lombok.Getter;

@Getter
public enum ScoringStrategyType {
    
    SIMPLE_LINEAR("SIMPLE_LINEAR", "Simple linear scoring where A=1, B=2, C=3, D=4, E=5 points"),
    WEIGHTED("WEIGHTED", "Weighted scoring for clinical assessments: A=0, B=1, C=3, D=6, E=10 points"),
    PERCENTAGE("PERCENTAGE", "Percentage-based scoring returning values from 0-100");

    private final String strategyName;
    private final String description;

    ScoringStrategyType(String strategyName, String description) {
        this.strategyName = strategyName;
        this.description = description;
    }

    public static ScoringStrategyType fromStrategyName(String strategyName) {
        for (ScoringStrategyType type : values()) {
            if (type.strategyName.equals(strategyName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown scoring strategy: " + strategyName);
    }
}