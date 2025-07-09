package com.hburak_dev.psk_full_stack.useranswer;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitTestRequest {

    @NotNull(message = "User test ID is required")
    private Integer userTestId;

    @NotEmpty(message = "Answers list cannot be empty")
    @Valid
    private List<SubmitAnswerRequest> answers;

}