package com.hburak_dev.psk_full_stack.choice;

import com.hburak_dev.psk_full_stack.question.AnswerType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PublicChoiceResponse {

    Integer id;

    private String text;

    private AnswerType answerType;

}
