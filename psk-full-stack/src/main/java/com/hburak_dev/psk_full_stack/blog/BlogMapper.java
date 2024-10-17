package com.hburak_dev.psk_full_stack.blog;

import org.springframework.stereotype.Service;

@Service
public class BlogMapper {
    public Blog toBlog(BlogRequest request) {
        return Blog.builder()
                .title(request.getTitle())
                .subTitle(request.getSubTitle())
                .text(request.getText())
                .cover(request.getCover())
                .shareable(request.isShareable())
                .build();
    }

    public BlogResponse toBlogResponse(Blog blog) {
        return BlogResponse.builder()
                .id(blog.getId())
                .createdDate(blog.getCreatedDate())
                .title(blog.getTitle())
                .subTitle(blog.getSubTitle())
                .text(blog.getText())
                .cover(blog.getCover())
                .shareable(blog.isShareable())
                .build();
    }

}
