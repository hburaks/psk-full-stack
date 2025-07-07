package com.hburak_dev.psk_full_stack.testtemplate;

import com.hburak_dev.psk_full_stack.comment.Comment;
import com.hburak_dev.psk_full_stack.exception.TestTemplateNotFoundException;
import com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes;
import com.hburak_dev.psk_full_stack.question.QuestionResponse;
import com.hburak_dev.psk_full_stack.question.QuestionServiceInterface;
import com.hburak_dev.psk_full_stack.usertest.UserTestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestTemplateServiceImpl implements TestTemplateServiceInterface {

    private final TestTemplateRepository testTemplateRepository;
    private final TestTemplateMapper testTemplateMapper;
    private final QuestionServiceInterface questionService;
    private final UserTestRepository userTestRepository;

    @Override
    @Transactional
    public TestTemplateResponse createTestTemplate(TestTemplateCreateRequest request) {
        TestTemplate testTemplate = testTemplateMapper.toTestTemplate(request);
        
        // Create comments from request if provided
        if (request.getComments() != null && !request.getComments().isEmpty()) {
            List<Comment> comments = new ArrayList<>();
            for (TestTemplateCommentRequest commentRequest : request.getComments()) {
                Comment comment = Comment.builder()
                        .testTemplateId(null) // Will be set after save
                        .score(commentRequest.getScore())
                        .title(commentRequest.getTitle())
                        .text(commentRequest.getText())
                        .imageUrl(commentRequest.getImageUrl())
                        .build();
                comments.add(comment);
            }
            testTemplate.setComments(comments);
        }
        
        TestTemplate savedTemplate = testTemplateRepository.save(testTemplate);
        
        // Update testTemplateId in comments after save
        if (savedTemplate.getComments() != null) {
            savedTemplate.getComments().forEach(comment -> 
                comment.setTestTemplateId(savedTemplate.getId().longValue()));
        }
        
        log.info("Test template created with id: {} with {} comments", 
                savedTemplate.getId(), 
                savedTemplate.getComments() != null ? savedTemplate.getComments().size() : 0);
        return testTemplateMapper.toTestTemplateResponse(savedTemplate);
    }

    @Override
    @Transactional
    public List<TestTemplateResponse> getAllTestTemplates() {
        List<TestTemplate> templates = testTemplateRepository.findAll();
        return templates.stream()
                .map(testTemplateMapper::toTestTemplateResponse)
                .toList();
    }

    @Override
    @Transactional
    public TestTemplateResponse getTestTemplateById(Integer id) {
        TestTemplate testTemplate = testTemplateRepository.findById(id)
                .orElseThrow(() -> new TestTemplateNotFoundException("Test template not found with id: " + id, BusinessErrorCodes.TEST_TEMPLATE_NOT_FOUND));
        return testTemplateMapper.toTestTemplateResponse(testTemplate);
    }

    @Override
    @Transactional
    public TestTemplateResponse updateTestTemplate(Integer id, TestTemplateUpdateRequest request) {
        TestTemplate existingTemplate = testTemplateRepository.findById(id)
                .orElseThrow(() -> new TestTemplateNotFoundException("Test template not found with id: " + id, BusinessErrorCodes.TEST_TEMPLATE_NOT_FOUND));
        
        existingTemplate.setTitle(request.getTitle());
        existingTemplate.setSubTitle(request.getSubTitle());
        existingTemplate.setImageUrl(request.getImageUrl());
        existingTemplate.setIsActive(request.getIsActive());
        if (request.getScoringStrategy() != null) {
            existingTemplate.setScoringStrategy(request.getScoringStrategy());
        }
        
        // Update comments if provided
        if (request.getComments() != null) {
            // Initialize comments list if null
            if (existingTemplate.getComments() == null) {
                existingTemplate.setComments(new ArrayList<>());
            } else {
                // Clear existing comments (orphanRemoval = true will handle deletion)
                existingTemplate.getComments().clear();
            }
            
            // Add new comments
            for (TestTemplateCommentRequest commentRequest : request.getComments()) {
                Comment comment = Comment.builder()
                        .testTemplateId(existingTemplate.getId().longValue())
                        .score(commentRequest.getScore())
                        .title(commentRequest.getTitle())
                        .text(commentRequest.getText())
                        .imageUrl(commentRequest.getImageUrl())
                        .build();
                existingTemplate.getComments().add(comment);
            }
        }
        
        TestTemplate updatedTemplate = testTemplateRepository.save(existingTemplate);
        
        log.info("Test template updated with id: {} with {} comments", 
                updatedTemplate.getId(), 
                updatedTemplate.getComments() != null ? updatedTemplate.getComments().size() : 0);
        return testTemplateMapper.toTestTemplateResponse(updatedTemplate);
    }

    @Override
    @Transactional
    public void deleteTestTemplate(Integer id) {
        if (!testTemplateRepository.existsById(id)) {
            throw new TestTemplateNotFoundException("Test template not found with id: " + id, BusinessErrorCodes.TEST_TEMPLATE_NOT_FOUND);
        }
        
        // Comments will be automatically deleted due to cascade = CascadeType.ALL and orphanRemoval = true
        testTemplateRepository.deleteById(id);
        log.info("Test template deleted with id: {}", id);
    }

    @Override
    @Transactional
    public List<QuestionResponse> getTestTemplateQuestions(Integer id) {
        if (!testTemplateRepository.existsById(id)) {
            throw new TestTemplateNotFoundException("Test template not found with id: " + id, BusinessErrorCodes.TEST_TEMPLATE_NOT_FOUND);
        }
        return questionService.getQuestionsByTestTemplate(id.longValue());
    }

    @Override
    @Transactional
    public List<TestTemplateResponse> getAvailableTestTemplatesForUser(Long userId) {
        List<TestTemplate> allTemplates = testTemplateRepository.findByIsActive(true);

        return allTemplates.stream()
                .filter(template -> {
                    Optional<com.hburak_dev.psk_full_stack.usertest.UserTest> existingAssignment =
                            userTestRepository.findByUserIdAndTestTemplateId(userId, template.getId().longValue());
                    return existingAssignment.isEmpty() || existingAssignment.get().getIsCompleted();
                })
                .map(testTemplateMapper::toTestTemplateResponse)
                .toList();
    }

    // Repository methods (keep existing functionality)
    public List<TestTemplate> findAll() {
        return testTemplateRepository.findAll();
    }

    public Optional<TestTemplate> findById(Integer id) {
        return testTemplateRepository.findById(id);
    }

    public TestTemplate save(TestTemplate testTemplate) {
        return testTemplateRepository.save(testTemplate);
    }

    public void deleteById(Integer id) {
        testTemplateRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return testTemplateRepository.existsById(id);
    }

    public List<TestTemplate> findByIsActive(Boolean isActive) {
        return testTemplateRepository.findByIsActive(isActive);
    }
}