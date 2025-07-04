package com.hburak_dev.psk_full_stack.useranswer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SubmitTestRequest {

    @NotNull(message = "User test ID is required")
    private Integer userTestId;

    @NotEmpty(message = "Answers list cannot be empty")
    @Valid
    private List<SubmitAnswerRequest> answers;

    private String personalNotes;
}