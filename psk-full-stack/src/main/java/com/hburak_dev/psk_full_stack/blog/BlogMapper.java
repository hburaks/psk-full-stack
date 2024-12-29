package com.hburak_dev.psk_full_stack.blog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.hburak_dev.psk_full_stack.service.FileStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogMapper {

    @Value("${server.port}")
    private String serverPort;

    public Blog toBlog(BlogRequest request) {
        return Blog.builder()
                .title(request.getTitle())
                .subTitle(request.getSubTitle())
                .text(request.getText())
                .shareable(request.isShareable())
                .build();
    }

    private BlogResponse toBlogResponse(Blog blog) {
        String imageUrl = null;
        if (blog.getImageFileName() != null) {
            imageUrl = String.format("http://localhost:%s/api/v3/files/blog/download/%s",
                    serverPort, blog.getImageFileName());
        }

        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .text(blog.getText())
                .subTitle(blog.getSubTitle())
                .shareable(blog.isShareable())
                .imageUrl(imageUrl)
                .build();
    }

}
