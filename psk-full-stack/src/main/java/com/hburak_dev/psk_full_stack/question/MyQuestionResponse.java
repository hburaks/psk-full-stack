package com.hburak_dev.psk_full_stack.question;

import com.hburak_dev.psk_full_stack.choice.MyChoiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MyQuestionResponse {

    private Integer id;

    private String text;

    private List<MyChoiceResponse> choices;

}
