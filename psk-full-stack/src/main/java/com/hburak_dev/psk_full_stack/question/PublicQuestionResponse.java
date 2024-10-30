package com.hburak_dev.psk_full_stack.question;

import com.hburak_dev.psk_full_stack.choice.PublicChoiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PublicQuestionResponse {

    private Integer id;

    private String text;

    private List<PublicChoiceResponse> publicChoiceResponseList;
}
