package com.hburak_dev.psk_full_stack.scoring;

import com.hburak_dev.psk_full_stack.question.AnswerType;
import com.hburak_dev.psk_full_stack.useranswer.SubmitAnswerRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SimpleLinearScoreStrategy implements ScoreCalculationStrategy {

    @Override
    public int calculateScore(List<SubmitAnswerRequest> answers) {
        // Count answers by type and calculate score
        Map<AnswerType, Long> answerCounts = answers.stream()
                .filter(answer -> answer.getAnswerType() != null)
                .collect(Collectors.groupingBy(
                        SubmitAnswerRequest::getAnswerType,
                        Collectors.counting()
                ));

        // Simple scoring: Each A=1, B=2, C=3, D=4, E=5
        int score = 0;
        for (Map.Entry<AnswerType, Long> entry : answerCounts.entrySet()) {
            int points = switch (entry.getKey()) {
                case ANSWER_A -> 1;
                case ANSWER_B -> 2;
                case ANSWER_C -> 3;
                case ANSWER_D -> 4;
                case ANSWER_E -> 5;
            };
            score += points * entry.getValue().intValue();
        }
        
        return score;
    }

    @Override
    public String getStrategyName() {
        return "SIMPLE_LINEAR";
    }

    @Override
    public String getDescription() {
        return "Simple linear scoring where A=1, B=2, C=3, D=4, E=5 points";
    }

    @Override
    public String getComment(int score, int totalQuestions) {
        if (totalQuestions == 0) {
            return "No questions in this test.";
        }
        // Assuming max score per question is 5 (for ANSWER_E)
        double maxPossibleScore = totalQuestions * 5.0;
        double percentage = (maxPossibleScore > 0) ? (score / maxPossibleScore) * 100 : 0;

        if (percentage >= 80) {
            return "Excellent performance based on linear scoring.";
        } else if (percentage >= 60) {
            return "Good performance based on linear scoring.";
        } else if (percentage >= 40) {
            return "Average performance based on linear scoring.";
        } else {
            return "Needs improvement based on linear scoring.";
        }
    }
}
