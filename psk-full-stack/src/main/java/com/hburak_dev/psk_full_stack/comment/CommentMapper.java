package com.hburak_dev.psk_full_stack.comment;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommentMapper {
    @Value("${server.port}")
    private String serverPort;

    public PublicTestAnswerCommentResponse toPublicTestAnswerCommentResponse(Comment resultComment) {
        String imageUrl = null;
        if (resultComment.getImageUrl() != null) {
            imageUrl = String.format("http://localhost:%s/api/v3/files/comment/download/%s",
                    serverPort, resultComment.getImageUrl());
        }
        return PublicTestAnswerCommentResponse.builder()
                .title(resultComment.getTitle())
                .text(resultComment.getText())
                .imageUrl(imageUrl)
                .build();
    }

    public Comment toComment(AdminTestCommentRequest adminTestCommentRequest) {
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
        String imageUrl = null;
        if (comment.getImageUrl() != null) {
            imageUrl = String.format("http://localhost:%s/api/v3/files/comment/download/%s",
                    serverPort, comment.getImageUrl());
        }
        return UserCommentResponse.builder()
                .score(comment.getScore())
                .title(comment.getTitle())
                .text(comment.getText())
                .imageUrl(imageUrl)
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
