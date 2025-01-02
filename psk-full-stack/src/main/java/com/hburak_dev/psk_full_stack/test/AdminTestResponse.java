package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.comment.AdminTestCommentResponse;
import com.hburak_dev.psk_full_stack.question.PublicQuestionAdminResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AdminTestResponse {

    private Integer id;

    private String title;

    private String subTitle;

    private String imageUrl;
    private Boolean isActive;

    private List<PublicQuestionAdminResponse> questions;

    private List<AdminTestCommentResponse> comments;

}
