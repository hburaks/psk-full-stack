package com.hburak_dev.psk_full_stack.testtemplate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TestTemplateResponse {

    private Integer id;
    private String title;
    private String subTitle;
    private String imageUrl;
    private Boolean isActive;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;
}