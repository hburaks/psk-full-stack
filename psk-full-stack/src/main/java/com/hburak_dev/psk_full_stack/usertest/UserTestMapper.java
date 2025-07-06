package com.hburak_dev.psk_full_stack.usertest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserTestMapper {

    @Value("${server.port}")
    private String serverPort;

    public UserTestResponse toUserTestResponse(UserTest userTest) {
        String imageUrl = null;
        if (userTest.getTestTemplate() != null && userTest.getTestTemplate().getImageUrl() != null) {
            imageUrl = String.format("http://localhost:%s/api/v3/files/test-template/download/%s",
                    serverPort, userTest.getTestTemplate().getImageUrl());
        }

        return UserTestResponse.builder()
                .id(userTest.getId())
                .userId(userTest.getUserId())
                .testTemplateId(userTest.getTestTemplateId())
                .assignedAt(userTest.getAssignedAt())
                .completedAt(userTest.getCompletedAt())
                .isCompleted(userTest.getIsCompleted())
                .assignedBy(userTest.getAssignedBy())
                
                // User information
                .userFirstname(userTest.getUser() != null ? userTest.getUser().getFirstname() : null)
                .userLastname(userTest.getUser() != null ? userTest.getUser().getLastname() : null)
                .userEmail(userTest.getUser() != null ? userTest.getUser().getEmail() : null)
                
                // Test template information
                .testTemplateTitle(userTest.getTestTemplate() != null ? userTest.getTestTemplate().getTitle() : null)
                .testTemplateSubTitle(userTest.getTestTemplate() != null ? userTest.getTestTemplate().getSubTitle() : null)
                .testTemplateImageUrl(imageUrl)
                
                // Assigned by information
                .assignedByFirstname(userTest.getAssignedByUser() != null ? userTest.getAssignedByUser().getFirstname() : null)
                .assignedByLastname(userTest.getAssignedByUser() != null ? userTest.getAssignedByUser().getLastname() : null)
                
                // Audit fields
                .createdDate(userTest.getCreatedDate())
                .lastModifiedDate(userTest.getLastModifiedDate())
                .createdBy(userTest.getCreatedBy())
                .lastModifiedBy(userTest.getLastModifiedBy())
                .build();
    }

    public UserTestListResponse toUserTestListResponse(UserTest userTest) {
        String imageUrl = null;
        if (userTest.getTestTemplate() != null && userTest.getTestTemplate().getImageUrl() != null) {
            imageUrl = String.format("http://localhost:%s/api/v3/files/test-template/download/%s",
                    serverPort, userTest.getTestTemplate().getImageUrl());
        }

        return UserTestListResponse.builder()
                .id(userTest.getId())
                .userId(userTest.getUserId())
                .testTemplateId(userTest.getTestTemplateId())
                .assignedAt(userTest.getAssignedAt())
                .completedAt(userTest.getCompletedAt())
                .isCompleted(userTest.getIsCompleted())
                
                // Test template information
                .testTemplateTitle(userTest.getTestTemplate() != null ? userTest.getTestTemplate().getTitle() : null)
                .testTemplateSubTitle(userTest.getTestTemplate() != null ? userTest.getTestTemplate().getSubTitle() : null)
                .testTemplateImageUrl(imageUrl)
                
                // User information (for admin view)
                .userFirstname(userTest.getUser() != null ? userTest.getUser().getFirstname() : null)
                .userLastname(userTest.getUser() != null ? userTest.getUser().getLastname() : null)
                .userEmail(userTest.getUser() != null ? userTest.getUser().getEmail() : null)
                .build();
    }
}