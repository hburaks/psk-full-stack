package com.hburak_dev.psk_full_stack.blog;

import com.hburak_dev.psk_full_stack.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("v2/blogs")
@RequiredArgsConstructor
@Tag(name = "Blog")
public class BlogControllerV2 {

    private final BlogServiceImpl service;

    @PostMapping("/save")
    public ResponseEntity<Integer> saveBlog(
            @ModelAttribute BlogRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.saveBlog(request, connectedUser));
    }

    @PostMapping("/update-shareable-status")
    public ResponseEntity<Integer> updateShareableStatus(
            @RequestParam("blogId") Integer blogId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.updateShareableStatus(blogId, connectedUser));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Integer> updateBlog(
            @PathVariable Integer id,
            @ModelAttribute BlogRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(service.updateSelectedBlog(id, request, authentication));
    }

    @GetMapping("/get-all-blogs")
    public ResponseEntity<PageResponse<BlogResponse>> findAllBlogs(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(service.findAllBlogs(page, size));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteBlog(@PathVariable Integer id) {
        return service.removeSelectedBlog(id);
    }


}
