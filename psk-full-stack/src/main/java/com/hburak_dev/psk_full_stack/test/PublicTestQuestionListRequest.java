package com.hburak_dev.psk_full_stack.test;


import com.hburak_dev.psk_full_stack.question.PublicTestQuestionRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PublicTestQuestionListRequest {

    private Integer testId;

    private List<PublicTestQuestionRequest> publicTestQuestionRequestList;

}
