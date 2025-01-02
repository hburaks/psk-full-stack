package com.hburak_dev.psk_full_stack.test;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class PublicTestRequest {

    private Integer testId;

    private String title;

    private String subTitle;

    private Boolean isActive;

}
