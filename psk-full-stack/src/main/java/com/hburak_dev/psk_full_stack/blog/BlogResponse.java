package com.hburak_dev.psk_full_stack.blog;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BlogResponse {

    private Integer id;

    private LocalDateTime createdDate;

    private String title;

    private String subTitle;

    private String text;

    private boolean shareable;

    private String imageUrl;

}
