package com.hburak_dev.psk_full_stack.question;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MyAnswerQuestionRequest {

    private Integer id;

    private AnswerType chosenAnswer;

}
