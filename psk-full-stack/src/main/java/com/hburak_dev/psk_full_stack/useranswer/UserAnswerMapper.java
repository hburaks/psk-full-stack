package com.hburak_dev.psk_full_stack.useranswer;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAnswerMapper {

    public UserAnswerResponse toUserAnswerResponse(UserAnswer userAnswer) {
        return UserAnswerResponse.builder()
                .id(userAnswer.getId())
                .userTestId(userAnswer.getUserTestId())
                .questionId(userAnswer.getQuestionId())
                .choiceId(userAnswer.getChoiceId())
                .textAnswer(userAnswer.getTextAnswer())
                .answeredAt(userAnswer.getAnsweredAt())
                
                // Question information
                .questionText(userAnswer.getQuestion() != null ? userAnswer.getQuestion().getText() : null)
                .questionOrderIndex(userAnswer.getQuestion() != null ? userAnswer.getQuestion().getOrderIndex() : null)
                .questionAnswerType(userAnswer.getQuestion() != null ? userAnswer.getQuestion().getUserAnswer() : null)
                
                // Choice information (if applicable)
                .choiceAnswerType(userAnswer.getChoice() != null ? userAnswer.getChoice().getAnswerType() : null)
                .choiceText(userAnswer.getChoice() != null ? userAnswer.getChoice().getText() : null)
                
                // UserTest information
                .testTemplateTitle(userAnswer.getUserTest() != null && userAnswer.getUserTest().getTestTemplate() != null ? 
                        userAnswer.getUserTest().getTestTemplate().getTitle() : null)
                .isTestCompleted(userAnswer.getUserTest() != null ? userAnswer.getUserTest().getIsCompleted() : null)
                
                // Audit fields
                .createdDate(userAnswer.getCreatedDate())
                .lastModifiedDate(userAnswer.getLastModifiedDate())
                .createdBy(userAnswer.getCreatedBy())
                .lastModifiedBy(userAnswer.getLastModifiedBy())
                .build();
    }
}