package com.hburak_dev.psk_full_stack.usertest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserTestListResponse {

    private Integer id;
    private Long userId;
    private Long testTemplateId;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
    private Boolean isCompleted;
    
    // Test template information (essential for list view)
    private String testTemplateTitle;
    private String testTemplateSubTitle;
    private String testTemplateImageUrl;
    
    // User information (for admin view)
    private String userFirstname;
    private String userLastname;
    private String userEmail;
}