package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.question.MyQuestionResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MyTestResponse {

    private Integer testId;

    private String title;

    private String subTitle;

    private String imageUrl;

    private List<MyQuestionResponse> questions;

}
