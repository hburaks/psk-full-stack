package com.hburak_dev.psk_full_stack.question;

import com.hburak_dev.psk_full_stack.choice.Choice;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class QuestionResponse {

    private Integer id;
    private String text;
    private Long testTemplateId;
    private Integer orderIndex;
    private AnswerType userAnswer;
    
    // Test template information
    private String testTemplateTitle;
    
    // Choice information
    private List<Choice> choices;
    
    // Audit fields
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Integer createdBy;
    private Integer lastModifiedBy;
}