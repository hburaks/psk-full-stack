package com.hburak_dev.psk_full_stack.question;

import com.hburak_dev.psk_full_stack.test.PublicChoiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PublicTestQuestionRequest {

    private Integer id;

    private String text;

    private List<PublicChoiceRequest> publicChoiceRequestList;

}
