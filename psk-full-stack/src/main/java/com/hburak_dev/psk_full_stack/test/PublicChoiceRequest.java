package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.question.AnswerType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PublicChoiceRequest {

    private Integer id;

    private AnswerType answerType;

    private String text;

}
