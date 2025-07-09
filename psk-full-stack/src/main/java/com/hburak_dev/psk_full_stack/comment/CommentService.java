package com.hburak_dev.psk_full_stack.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;


    @Transactional(readOnly = true)
    public List<Comment> getCommentsForTestTemplate(Long testTemplateId) {
        return commentRepository.findByTestTemplateIdOrderByScoreAsc(testTemplateId);
    }

    @Transactional
    public Comment createComment(Long testTemplateId, Integer score, String title, String text, String imageUrl) {
        Comment comment = Comment.builder()
                .testTemplateId(testTemplateId)
                .score(score)
                .title(title)
                .text(text)
                .imageUrl(imageUrl)
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
        log.info("Comment deleted with id: {}", commentId);
    }

    @Transactional
    public void deleteCommentsByTestTemplate(Long testTemplateId) {
        List<Comment> comments = commentRepository.findByTestTemplateIdOrderByScoreAsc(testTemplateId);
        commentRepository.deleteAll(comments);
        log.info("Deleted {} comments for test template: {}", comments.size(), testTemplateId);
    }
}