package com.hburak_dev.psk_full_stack.blog;

import com.hburak_dev.psk_full_stack.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("blogs")
@RequiredArgsConstructor
@Tag(name = "Blog")
public class BlogController {

    private final BlogServiceImpl service;

    @GetMapping("/get-all-blogs")
    public ResponseEntity<PageResponse<BlogResponse>> findAllBlogsShareable(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(service.findAllBlogsShareable(page, size));
    }

}
