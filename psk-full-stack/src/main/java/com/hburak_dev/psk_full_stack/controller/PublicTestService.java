package com.hburak_dev.psk_full_stack.controller;

import com.hburak_dev.psk_full_stack.comment.CommentRepository;
import com.hburak_dev.psk_full_stack.comment.PublicTestAnswerCommentResponse;
import com.hburak_dev.psk_full_stack.comment.Comment;
import com.hburak_dev.psk_full_stack.question.QuestionResponse;
import com.hburak_dev.psk_full_stack.scoring.ScoreCalculationService;
import com.hburak_dev.psk_full_stack.scoring.ScoringStrategyType;
import com.hburak_dev.psk_full_stack.testtemplate.TestTemplateResponse;
import com.hburak_dev.psk_full_stack.testtemplate.TestTemplateServiceInterface;
import com.hburak_dev.psk_full_stack.useranswer.SubmitAnswerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicTestService {

    private final TestTemplateServiceInterface testTemplateService;
    private final CommentRepository commentRepository;
    private final ScoreCalculationService scoreCalculationService;

    @Transactional(readOnly = true)
    public List<TestTemplateResponse> getAllActiveTestTemplates() {
        return testTemplateService.getAllTestTemplates()
                .stream()
                .filter(TestTemplateResponse::getIsActive)
                .toList();
    }

    @Transactional(readOnly = true)
    public TestTemplateResponse getActiveTestTemplateById(Integer id) {
        TestTemplateResponse template = testTemplateService.getTestTemplateById(id);
        if (!template.getIsActive()) {
            throw new IllegalArgumentException("Test template is not active");
        }
        return template;
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getTestTemplateQuestions(Integer id) {
        getActiveTestTemplateById(id); // Validate template exists and is active
        return testTemplateService.getTestTemplateQuestions(id);
    }

    @Transactional(readOnly = true)
    public PublicTestResultResponse processTestSubmission(Integer testTemplateId, List<SubmitAnswerRequest> answers) {
        TestTemplateResponse template = getActiveTestTemplateById(testTemplateId);
        List<QuestionResponse> questions = testTemplateService.getTestTemplateQuestions(testTemplateId);
        
        // Calculate basic statistics without saving to database
        int totalQuestions = questions.size();
        int answeredQuestions = answers.size();
        
        // Determine scoring strategy from test template
        ScoringStrategyType scoringStrategy = template.getScoringStrategy() != null ? 
                template.getScoringStrategy() : ScoringStrategyType.SIMPLE_LINEAR;
        
        // Calculate score internally for comment selection (but don't return it to user)
        int score = scoreCalculationService.calculateScore(answers, scoringStrategy);
        
        // Get appropriate comment based on score
        PublicTestAnswerCommentResponse comment = getCommentForScore(score, testTemplateId.longValue());
        
        log.info("Public test submission processed for template: {}, questions: {}, answers: {}, score: {} (not shown to user)", 
                testTemplateId, totalQuestions, answeredQuestions, score);
        
        return PublicTestResultResponse.builder()
                .testTemplateId(testTemplateId)
                .testTitle(template.getTitle())
                .totalQuestions(totalQuestions)
                .answeredQuestions(answeredQuestions)
                .comment(comment)
                .submittedAt(java.time.LocalDateTime.now())
                .message("Test completed successfully. Thank you for your participation!")
                .build();
    }


    private PublicTestAnswerCommentResponse getCommentForScore(int score, Long testTemplateId) {
        // Find comment based on score range for specific test template
        List<Comment> comments = commentRepository.findByTestTemplateIdAndScoreLessThanEqualOrderByScoreDesc(testTemplateId, score);
        
        Comment selectedComment = comments.stream()
                .findFirst()
                .orElse(getDefaultCommentForTestTemplate(testTemplateId));
        
        return PublicTestAnswerCommentResponse.builder()
                .title(selectedComment.getTitle())
                .text(selectedComment.getText())
                .imageUrl(selectedComment.getImageUrl())
                .build();
    }

    private Comment getDefaultCommentForTestTemplate(Long testTemplateId) {
        // Try to find any comment for this test template
        List<Comment> testTemplateComments = commentRepository.findByTestTemplateIdOrderByScoreAsc(testTemplateId);
        
        return testTemplateComments.stream()
                .findFirst()
                .orElse(createDefaultComment());
    }

    private Comment createDefaultComment() {
        return Comment.builder()
                .title("Test Completed")
                .text("Thank you for completing the test. Your responses have been processed.")
                .build();
    }
}