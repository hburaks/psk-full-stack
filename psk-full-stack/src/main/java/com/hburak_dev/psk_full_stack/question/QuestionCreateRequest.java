package com.hburak_dev.psk_full_stack.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class QuestionCreateRequest {

    @NotNull(message = "Test template ID is required")
    private Long testTemplateId;

    @NotBlank(message = "Question text is required")
    @Size(max = 1000, message = "Question text must not exceed 1000 characters")
    private String text;

    private List<Integer> choiceIds;

    private Integer orderIndex;
}