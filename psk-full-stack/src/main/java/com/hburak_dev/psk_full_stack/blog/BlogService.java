package com.hburak_dev.psk_full_stack.blog;

import com.hburak_dev.psk_full_stack.common.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface BlogService {

    Integer saveBlog(BlogRequest request, Authentication connectedUser);

    PageResponse<BlogResponse> findAllBlogsShareable(int page, int size);

    PageResponse<BlogResponse> findAllBlogs(int page, int size);

    Integer updateSelectedBlog(Integer id, BlogRequest request, Authentication authentication);

    ResponseEntity<Boolean> removeSelectedBlog(Integer id);

    Integer updateShareableStatus(Integer id, Authentication connectedUser);

    BlogResponse findBlogById(Integer id);
}
