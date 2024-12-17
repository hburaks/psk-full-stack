package com.hburak_dev.psk_full_stack.comment;


import java.util.Arrays;
import com.hburak_dev.psk_full_stack.test.Test;

import org.springframework.stereotype.Service;

@Service
public class CommentMapper {
    public PublicTestAnswerCommentResponse toPublicTestAnswerCommentResponse(Comment resultComment) {
        return PublicTestAnswerCommentResponse.builder()
                .title(resultComment.getTitle())
                .text(resultComment.getText())
                .cover(resultComment.getCover())
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

        if (adminTestCommentRequest.getCover() != null) {
            comment.setCover(adminTestCommentRequest.getCover());
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

        if (adminTestCommentRequest.getCover() != null) {
            comment.setCover(adminTestCommentRequest.getCover());
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
                .cover(comment.getCover())
                .build();
    }

    public AdminTestCommentResponse toAdminCommentResponse(Comment comment) {
        return AdminTestCommentResponse.builder()
                .commentId(comment.getId())
                .score(comment.getScore())
                .title(comment.getTitle())
                .text(comment.getText())
                .cover(comment.getCover())
                .build();
    }
}
