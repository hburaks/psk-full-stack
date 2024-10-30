package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.comment.UserCommentResponse;
import com.hburak_dev.psk_full_stack.question.UserQuestionResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class UserTestResponse {

    private Integer testId;


    private String title;

    private String subTitle;

    byte[] cover;

    private List<UserQuestionResponse> questions;

    private Integer userId;

    private List<UserCommentResponse> comments;

}
