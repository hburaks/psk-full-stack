package com.hburak_dev.psk_full_stack.controller;

import com.hburak_dev.psk_full_stack.comment.PublicTestAnswerCommentResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PublicTestResultResponse {
    
    private Integer testTemplateId;
    private String testTitle;
    private Integer totalQuestions;
    private Integer answeredQuestions;
    private Integer score;
    private PublicTestAnswerCommentResponse comment;
    private LocalDateTime submittedAt;
    private String message;
}