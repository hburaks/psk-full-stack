package com.hburak_dev.psk_full_stack.testtemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestTemplateMapper {

    @Value("${server.port}")
    private String serverPort;

    public TestTemplate toTestTemplate(TestTemplateCreateRequest request) {
        return TestTemplate.builder()
                .title(request.getTitle())
                .subTitle(request.getSubTitle())
                .imageUrl(request.getImageUrl())
                .isActive(request.getIsActive())
                .build();
    }

    public TestTemplate toTestTemplate(TestTemplateUpdateRequest request) {
        return TestTemplate.builder()
                .title(request.getTitle())
                .subTitle(request.getSubTitle())
                .imageUrl(request.getImageUrl())
                .isActive(request.getIsActive())
                .build();
    }

    public TestTemplateResponse toTestTemplateResponse(TestTemplate testTemplate) {
        String imageUrl = null;
        if (testTemplate.getImageUrl() != null) {
            imageUrl = String.format("http://localhost:%s/api/v3/files/test-template/download/%s",
                    serverPort, testTemplate.getImageUrl());
        }

        return TestTemplateResponse.builder()
                .id(testTemplate.getId())
                .title(testTemplate.getTitle())
                .subTitle(testTemplate.getSubTitle())
                .imageUrl(imageUrl)
                .imagePath(testTemplate.getImageUrl())
                .isActive(testTemplate.getIsActive())
                .scoringStrategy(testTemplate.getScoringStrategy())
                .createdDate(testTemplate.getCreatedDate())
                .lastModifiedDate(testTemplate.getLastModifiedDate())
                .createdBy(testTemplate.getCreatedBy() != null ? testTemplate.getCreatedBy().toString() : null)
                .lastModifiedBy(testTemplate.getLastModifiedBy() != null ? testTemplate.getLastModifiedBy().toString() : null)
                .build();
    }
}