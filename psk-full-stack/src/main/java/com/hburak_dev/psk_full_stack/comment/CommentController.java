package com.hburak_dev.psk_full_stack.comment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v2/admin/comments")
@RequiredArgsConstructor
@Tag(name = "Comment Admin", description = "Admin endpoints for managing test template comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping("/test-template/{testTemplateId}")
    @Operation(summary = "Get all comments for a test template")
    public ResponseEntity<List<AdminTestCommentResponse>> getCommentsByTestTemplate(@PathVariable Long testTemplateId) {
        List<Comment> comments = commentService.getCommentsForTestTemplate(testTemplateId);
        List<AdminTestCommentResponse> responses = comments.stream()
                .map(commentMapper::toAdminTestCommentResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/test-template/{testTemplateId}")
    @Operation(summary = "Create a new comment for a test template")
    public ResponseEntity<AdminTestCommentResponse> createComment(
            @PathVariable Long testTemplateId,
            @Valid @RequestBody AdminTestCommentRequest request,
            Authentication connectedUser) {
        
        Comment comment = commentService.createComment(
                testTemplateId,
                request.getScore(),
                request.getTitle(),
                request.getText(),
                null, // imageUrl will be handled separately if needed
                connectedUser
        );
        
        AdminTestCommentResponse response = commentMapper.toAdminTestCommentResponse(comment);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/test-template/{testTemplateId}")
    @Operation(summary = "Update all comments for a test template", 
               description = "Update all comments for a test template in bulk")
    public ResponseEntity<List<AdminTestCommentResponse>> updateTestTemplateComments(
            @PathVariable Long testTemplateId,
            @Valid @RequestBody List<AdminTestCommentRequest> comments,
            Authentication connectedUser) {
        
        List<Comment> updatedComments = commentService.updateTestTemplateComments(testTemplateId, comments, connectedUser);
        List<AdminTestCommentResponse> responses = updatedComments.stream()
                .map(commentMapper::toAdminTestCommentResponse)
                .toList();
        
        return ResponseEntity.ok(responses);
    }

}