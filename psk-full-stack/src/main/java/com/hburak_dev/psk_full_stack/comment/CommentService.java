package com.hburak_dev.psk_full_stack.comment;

import com.hburak_dev.psk_full_stack.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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
    public Comment createComment(Long testTemplateId, Integer score, String title, String text, String imageUrl, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Comment comment = Comment.builder()
                .testTemplateId(testTemplateId)
                .score(score)
                .title(title)
                .text(text)
                .imageUrl(imageUrl)
                .createdBy(user.getId())
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

    @Transactional
    public List<Comment> updateTestTemplateComments(Long testTemplateId, List<AdminTestCommentRequest> commentRequests, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        
        // Önce mevcut comment'ları sil
        deleteCommentsByTestTemplate(testTemplateId);
        
        // Yeni comment'ları kaydet
        List<Comment> savedComments = commentRequests.stream()
                .map(request -> Comment.builder()
                        .testTemplateId(testTemplateId)
                        .score(request.getScore())
                        .title(request.getTitle())
                        .text(request.getText())
                        .imageUrl(null) // Image URL'i ayrı olarak handle edilecek
                        .createdBy(user.getId())
                        .build())
                .map(commentRepository::save)
                .collect(java.util.stream.Collectors.toList());
        
        log.info("Updated {} comments for test template: {}", savedComments.size(), testTemplateId);
        return savedComments;
    }
}