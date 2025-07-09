package com.hburak_dev.psk_full_stack.usertest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignTestRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Test template ID is required")
    private Long testTemplateId;

}