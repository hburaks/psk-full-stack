package com.hburak_dev.psk_full_stack.blog;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class BlogRequest {

    private String title;

    private String subTitle;

    private String text;

    private boolean shareable;

    private MultipartFile image;

}
