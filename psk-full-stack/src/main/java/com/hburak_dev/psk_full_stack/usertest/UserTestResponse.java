package com.hburak_dev.psk_full_stack.usertest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserTestResponse {

    private Integer id;
    private Long userId;
    private Long testTemplateId;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
    private Boolean isCompleted;
    private Long assignedBy;
    
    // User information
    private String userFirstname;
    private String userLastname;
    private String userEmail;
    
    // Test template information
    private String testTemplateTitle;
    private String testTemplateSubTitle;
    private String testTemplateImageUrl;
    
    // Assigned by information
    private String assignedByFirstname;
    private String assignedByLastname;
    
    // Audit fields
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Integer createdBy;
    private Integer lastModifiedBy;
}