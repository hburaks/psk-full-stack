package com.hburak_dev.psk_full_stack.scoring;

import com.hburak_dev.psk_full_stack.useranswer.SubmitAnswerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScoreCalculationService {

    private final List<ScoreCalculationStrategy> strategies;

    public int calculateScore(List<SubmitAnswerRequest> answers, String strategyName) {
        ScoreCalculationStrategy strategy = getStrategy(strategyName);
        
        int score = strategy.calculateScore(answers);
        
        log.info("Calculated score: {} using strategy: {} for {} answers", 
                score, strategyName, answers.size());
        
        return score;
    }

    public int calculateScore(List<SubmitAnswerRequest> answers, ScoringStrategyType strategyType) {
        return calculateScore(answers, strategyType.getStrategyName());
    }

    public ScoreCalculationStrategy getStrategy(String strategyName) {
        Map<String, ScoreCalculationStrategy> strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        ScoreCalculationStrategy::getStrategyName,
                        Function.identity()
                ));

        ScoreCalculationStrategy strategy = strategyMap.get(strategyName);
        if (strategy == null) {
            log.warn("Strategy '{}' not found, using default SIMPLE_LINEAR", strategyName);
            strategy = strategyMap.get("SIMPLE_LINEAR");
        }
        
        return strategy;
    }

    public List<ScoreCalculationStrategy> getAllStrategies() {
        return strategies;
    }
}