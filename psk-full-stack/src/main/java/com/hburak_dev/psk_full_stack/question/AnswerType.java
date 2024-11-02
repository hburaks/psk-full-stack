package com.hburak_dev.psk_full_stack.question;

import lombok.Getter;

@Getter
public enum AnswerType {

    ANSWER_A("A"),
    ANSWER_B("B"),
    ANSWER_C("C"),
    ANSWER_D("D"),
    ANSWER_E("E");

    private final String answer;

    AnswerType(String answer) {
        this.answer = answer;
    }
}
