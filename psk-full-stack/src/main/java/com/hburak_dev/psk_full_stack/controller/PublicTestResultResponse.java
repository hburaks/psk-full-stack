package com.hburak_dev.psk_full_stack.controller;

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
    private LocalDateTime submittedAt;
    private String message;
}