package com.hburak_dev.psk_full_stack.controller;

import com.hburak_dev.psk_full_stack.useranswer.SubmitAnswerRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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
public class PublicTestSubmissionRequest {
    
    @NotEmpty(message = "Answers list cannot be empty")
    @Valid
    private List<SubmitAnswerRequest> answers;
    
}