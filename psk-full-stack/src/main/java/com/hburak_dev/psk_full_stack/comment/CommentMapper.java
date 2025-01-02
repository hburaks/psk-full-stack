package com.hburak_dev.psk_full_stack.comment;

import java.util.Arrays;
import com.hburak_dev.psk_full_stack.test.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommentMapper {
    @Value("${server.port}")
    private String serverPort;

    public PublicTestAnswerCommentResponse toPublicTestAnswerCommentResponse(Comment resultComment) {
        return PublicTestAnswerCommentResponse.builder()
                .title(resultComment.getTitle())
                .text(resultComment.getText())
                .imageUrl(resultComment.getImageUrl())
                .build();
    }

    public Comment toComment(AdminTestCommentRequest adminTestCommentRequest, Test test) {
        Comment comment = Comment.builder().build();

        if (adminTestCommentRequest.getText() != null) {
            comment.setText(adminTestCommentRequest.getText());
        }

        if (adminTestCommentRequest.getScore() != null) {
            comment.setScore(adminTestCommentRequest.getScore());
        }

        if (adminTestCommentRequest.getTitle() != null) {
            comment.setTitle(adminTestCommentRequest.getTitle());
        }

        if (adminTestCommentRequest.getCommentId() != null) {
            comment.setId(adminTestCommentRequest.getCommentId());
        }

        if (adminTestCommentRequest.getScore() != null) {
            comment.setScore(adminTestCommentRequest.getScore());
        }

        comment.setTests(Arrays.asList(test));

        return comment;
    }

    public Comment updateComment(Comment comment, AdminTestCommentRequest adminTestCommentRequest) {
        if (adminTestCommentRequest.getText() != null) {
            comment.setText(adminTestCommentRequest.getText());
        }

        if (adminTestCommentRequest.getScore() != null) {
            comment.setScore(adminTestCommentRequest.getScore());
        }

        if (adminTestCommentRequest.getTitle() != null) {
            comment.setTitle(adminTestCommentRequest.getTitle());
        }

        if (adminTestCommentRequest.getCommentId() != null) {
            comment.setId(adminTestCommentRequest.getCommentId());
        }

        if (adminTestCommentRequest.getScore() != null) {
            comment.setScore(adminTestCommentRequest.getScore());
        }

        return comment;
    }

    public UserCommentResponse toUserCommentResponse(Comment comment) {
        return UserCommentResponse.builder()
                .score(comment.getScore())
                .title(comment.getTitle())
                .text(comment.getText())
                .imageUrl(comment.getImageUrl())
                .build();
    }

    public AdminTestCommentResponse toAdminCommentResponse(Comment comment) {
        String imageUrl = null;
        if (comment.getImageUrl() != null) {
            imageUrl = String.format("http://localhost:%s/api/v3/files/comment/download/%s",
                    serverPort, comment.getImageUrl());
        }
        return AdminTestCommentResponse.builder()
                .commentId(comment.getId())
                .score(comment.getScore())
                .title(comment.getTitle())
                .text(comment.getText())
                .imageUrl(imageUrl)
                .build();
    }
}
