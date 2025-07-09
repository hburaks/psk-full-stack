package com.hburak_dev.psk_full_stack.useranswer;

import com.hburak_dev.psk_full_stack.question.AnswerType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserAnswerResponse {

    private Integer id;
    private Long userTestId;
    private Long questionId;
    private Long choiceId;
    private String textAnswer;
    private LocalDateTime answeredAt;
    
    // Question information
    private String questionText;
    private Integer questionOrderIndex;
    private AnswerType questionAnswerType;
    
    // Choice information (if applicable)
    private AnswerType choiceAnswerType;
    private String choiceText;
    
    // UserTest information
    private String testTemplateTitle;
    private Boolean isTestCompleted;
    
    // Audit fields
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Integer createdBy;
    private Integer lastModifiedBy;
}