package com.hburak_dev.psk_full_stack.controller;

import com.hburak_dev.psk_full_stack.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v3/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("/test/download/{fileName}")
    public ResponseEntity<Resource> downloadTestFile(@PathVariable String fileName) {
      Resource resource = fileStorageService.loadFileAsResource(fileName, "tests");
      return ResponseEntity.ok()
          .contentType(MediaType.IMAGE_JPEG)
          .body(resource);
    }

    @GetMapping("/blog/download/{fileName}")
    public ResponseEntity<Resource> downloadBlogFile(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName, "blogs");
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/comment/download/{fileName}")
    public ResponseEntity<Resource> downloadCommentImage(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName, "comments");
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/test-template/download/{fileName}")
    public ResponseEntity<Resource> downloadTestTemplateImage(@PathVariable String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName, "test-templates");
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}
