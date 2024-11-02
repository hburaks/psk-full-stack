package com.hburak_dev.psk_full_stack.comment;


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

    public Comment toComment(PublicTestCommentRequest publicTestCommentRequest) {
        return Comment.builder()
                .text(publicTestCommentRequest.getText())
                .score(publicTestCommentRequest.getScore())
                .build();
    }

    public UserCommentResponse toUserCommentResponse(Comment comment) {
        return UserCommentResponse.builder()
                .score(comment.getScore())
                .title(comment.getTitle())
                .text(comment.getText())
                .cover(comment.getCover())
                .build();
    }
}
