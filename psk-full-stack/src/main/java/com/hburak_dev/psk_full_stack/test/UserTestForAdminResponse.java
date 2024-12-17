package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.comment.UserCommentResponse;
import com.hburak_dev.psk_full_stack.question.AnswerType;
import com.hburak_dev.psk_full_stack.question.UserQuestionResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class UserTestForAdminResponse {

    private Integer testId;

    private String title;

    private String subTitle;

    private String[] cover;

    private Integer userId;

    private List<UserQuestionResponse> questions;

    private List<UserCommentResponse> comments;

    private Map<AnswerType, Long> answerDistribution;

    private LocalDateTime lastModifiedDate;
}
