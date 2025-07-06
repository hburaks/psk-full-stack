package com.hburak_dev.psk_full_stack.scoring;

import com.hburak_dev.psk_full_stack.question.AnswerType;
import com.hburak_dev.psk_full_stack.useranswer.SubmitAnswerRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WeightedScoreStrategy implements ScoreCalculationStrategy {

    @Override
    public int calculateScore(List<SubmitAnswerRequest> answers) {
        // Count answers by type and calculate weighted score
        Map<AnswerType, Long> answerCounts = answers.stream()
                .filter(answer -> answer.getAnswerType() != null)
                .collect(Collectors.groupingBy(
                        SubmitAnswerRequest::getAnswerType,
                        Collectors.counting()
                ));

        // Weighted scoring for more severe conditions
        int score = 0;
        for (Map.Entry<AnswerType, Long> entry : answerCounts.entrySet()) {
            int points = switch (entry.getKey()) {
                case ANSWER_A -> 0;  // No symptoms
                case ANSWER_B -> 1;  // Mild
                case ANSWER_C -> 3;  // Moderate  
                case ANSWER_D -> 6;  // Severe
                case ANSWER_E -> 10; // Very severe
            };
            score += points * entry.getValue().intValue();
        }
        
        return score;
    }

    @Override
    public String getStrategyName() {
        return "WEIGHTED";
    }

    @Override
    public String getDescription() {
        return "Weighted scoring for clinical assessments: A=0, B=1, C=3, D=6, E=10 points";
    }
}