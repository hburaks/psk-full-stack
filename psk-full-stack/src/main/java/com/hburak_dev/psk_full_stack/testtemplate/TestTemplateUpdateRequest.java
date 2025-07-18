package com.hburak_dev.psk_full_stack.testtemplate;

import com.hburak_dev.psk_full_stack.scoring.ScoringStrategyType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TestTemplateUpdateRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 500, message = "Subtitle must not exceed 500 characters")
    private String subTitle;

    private String imageUrl;

    private Boolean isActive;

    private ScoringStrategyType scoringStrategy;

    @Valid
    private List<TestTemplateCommentRequest> comments;
}