package com.hburak_dev.psk_full_stack.blog;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BlogRequest {

    private String title;

    private String subTitle;

    private String text;

    private boolean shareable;

}
