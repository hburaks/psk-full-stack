package com.hburak_dev.psk_full_stack.session;

import lombok.Getter;

@Getter
public enum SessionStatusType {

    ANSWER_A("A"),
    ANSWER_B("B"),
    ANSWER_C("C"),
    ANSWER_D("D");

    private final String answer;

    SessionStatusType(String answer) {
        this.answer = answer;
    }
}
