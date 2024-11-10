package com.hburak_dev.psk_full_stack.question;

import com.hburak_dev.psk_full_stack.choice.UserChoiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class UserQuestionResponse {

    private String text;

    private AnswerType userAnswer;

    private List<UserChoiceResponse> choices;

}
