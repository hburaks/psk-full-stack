package com.hburak_dev.psk_full_stack.question;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class PublicTestAnswerQuestionRequest {

    private Integer id;

    private AnswerType chosenAnswer;

}
