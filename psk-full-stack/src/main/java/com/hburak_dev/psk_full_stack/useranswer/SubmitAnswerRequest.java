package com.hburak_dev.psk_full_stack.useranswer;

import com.hburak_dev.psk_full_stack.question.AnswerType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubmitAnswerRequest {

    @NotNull(message = "User test ID is required")
    private Integer userTestId;

    @NotNull(message = "Question ID is required")
    private Integer questionId;

    // For multiple choice answers
    private Integer choiceId;
    private AnswerType answerType;

    // For text answers
    @Size(max = 2000, message = "Text answer must not exceed 2000 characters")
    private String textAnswer;
}