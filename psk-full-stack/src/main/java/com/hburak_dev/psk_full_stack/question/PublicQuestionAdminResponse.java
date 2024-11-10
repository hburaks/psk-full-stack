package com.hburak_dev.psk_full_stack.question;

import com.hburak_dev.psk_full_stack.choice.PublicChoiceAdminResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PublicQuestionAdminResponse {

    private Integer id;

    private String text;

    private List<PublicChoiceAdminResponse> publicChoiceResponseList;
}
