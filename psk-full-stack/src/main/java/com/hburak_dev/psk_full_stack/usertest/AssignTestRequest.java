package com.hburak_dev.psk_full_stack.usertest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AssignTestRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Test template ID is required")
    private Long testTemplateId;

}