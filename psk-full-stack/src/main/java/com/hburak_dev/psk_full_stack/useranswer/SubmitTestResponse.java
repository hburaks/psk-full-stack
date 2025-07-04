package com.hburak_dev.psk_full_stack.useranswer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class SubmitTestResponse {

    private Integer userTestId;
    private Boolean isCompleted;
    private LocalDateTime completedAt;
    private List<UserAnswerResponse> submittedAnswers;
    private String personalNotes;
}