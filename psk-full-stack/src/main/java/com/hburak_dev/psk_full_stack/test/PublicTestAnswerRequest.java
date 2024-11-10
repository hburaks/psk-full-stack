package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.question.PublicTestAnswerQuestionRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PublicTestAnswerRequest {

    private Integer testId;

    private List<PublicTestAnswerQuestionRequest> questions;

}
