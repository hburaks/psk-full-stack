package com.hburak_dev.psk_full_stack.choice;

import com.hburak_dev.psk_full_stack.question.AnswerType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserChoiceResponse {

    private AnswerType answerType;

    private boolean isSelected;

    private boolean isCorrect;

    private String text;

}
