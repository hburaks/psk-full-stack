package com.hburak_dev.psk_full_stack.scoring;

import com.hburak_dev.psk_full_stack.useranswer.SubmitAnswerRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PercentageScoreStrategy implements ScoreCalculationStrategy {

    @Override
    public int calculateScore(List<SubmitAnswerRequest> answers) {
        if (answers.isEmpty()) {
            return 0;
        }

        int totalAnswers = answers.size();
        int totalPossibleScore = totalAnswers * 5; // Maximum score if all answers were E (5 points)
        
        int actualScore = 0;
        for (SubmitAnswerRequest answer : answers) {
            if (answer.getAnswerType() != null) {
                int points = switch (answer.getAnswerType()) {
                    case ANSWER_A -> 1;
                    case ANSWER_B -> 2;
                    case ANSWER_C -> 3;
                    case ANSWER_D -> 4;
                    case ANSWER_E -> 5;
                };
                actualScore += points;
            }
        }
        
        // Return percentage score (0-100)
        return Math.round((actualScore * 100.0f) / totalPossibleScore);
    }

    @Override
    public String getStrategyName() {
        return "PERCENTAGE";
    }

    @Override
    public String getDescription() {
        return "Percentage-based scoring returning values from 0-100";
    }
}