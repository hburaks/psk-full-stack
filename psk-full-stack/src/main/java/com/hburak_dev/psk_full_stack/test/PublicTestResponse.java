package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.question.PublicQuestionResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class PublicTestResponse {

    private Integer id;

    private byte[] cover;

    private String title;

    private String subTitle;

    private List<PublicQuestionResponse> questions;

}
