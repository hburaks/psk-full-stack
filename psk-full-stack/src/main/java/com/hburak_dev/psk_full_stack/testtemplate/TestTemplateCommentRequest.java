package com.hburak_dev.psk_full_stack.testtemplate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TestTemplateCommentRequest {

    @NotNull(message = "Score is required")
    private Integer score;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Text is required")
    private String text;

    private String imageUrl;
}