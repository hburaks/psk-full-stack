package com.hburak_dev.psk_full_stack.controller;

import com.hburak_dev.psk_full_stack.question.QuestionResponse;
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
        TestTemplateResponse template = getActiveTestTemplateById(id);
        return testTemplateService.getTestTemplateQuestions(id);
    }

    @Transactional(readOnly = true)
    public PublicTestResultResponse processTestSubmission(Integer testTemplateId, List<SubmitAnswerRequest> answers) {
        TestTemplateResponse template = getActiveTestTemplateById(testTemplateId);
        List<QuestionResponse> questions = testTemplateService.getTestTemplateQuestions(testTemplateId);
        
        // Calculate basic statistics without saving to database
        int totalQuestions = questions.size();
        int answeredQuestions = answers.size();
        
        log.info("Public test submission processed for template: {}, questions: {}, answers: {}", 
                testTemplateId, totalQuestions, answeredQuestions);
        
        return PublicTestResultResponse.builder()
                .testTemplateId(testTemplateId)
                .testTitle(template.getTitle())
                .totalQuestions(totalQuestions)
                .answeredQuestions(answeredQuestions)
                .submittedAt(java.time.LocalDateTime.now())
                .message("Test completed successfully. Thank you for your participation!")
                .build();
    }
}