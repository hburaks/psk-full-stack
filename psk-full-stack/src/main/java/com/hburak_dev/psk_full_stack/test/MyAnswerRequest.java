package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.question.MyAnswerQuestionRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class MyAnswerRequest {


    private Integer testId;

    private List<MyAnswerQuestionRequest> questions;

}
