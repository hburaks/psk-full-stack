package com.hburak_dev.psk_full_stack.scoring;

import com.hburak_dev.psk_full_stack.useranswer.SubmitAnswerRequest;

import java.util.List;

public interface ScoreCalculationStrategy {
    
    int calculateScore(List<SubmitAnswerRequest> answers);
    
    String getStrategyName();
    
    String getDescription();
}