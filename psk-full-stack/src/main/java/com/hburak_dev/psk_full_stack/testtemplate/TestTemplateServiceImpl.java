package com.hburak_dev.psk_full_stack.testtemplate;

import com.hburak_dev.psk_full_stack.exception.TestTemplateNotFoundException;
import com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes;
import com.hburak_dev.psk_full_stack.question.QuestionServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestTemplateServiceImpl implements TestTemplateServiceInterface {

    private final TestTemplateRepository testTemplateRepository;
    private final TestTemplateMapper testTemplateMapper;
    private final QuestionServiceInterface questionService;

    @Override
    @Transactional
    public TestTemplateResponse createTestTemplate(TestTemplateCreateRequest request) {
        TestTemplate testTemplate = testTemplateMapper.toTestTemplate(request);
        TestTemplate savedTemplate = testTemplateRepository.save(testTemplate);
        log.info("Test template created with id: {}", savedTemplate.getId());
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
        
        TestTemplate updatedTemplate = testTemplateRepository.save(existingTemplate);
        log.info("Test template updated with id: {}", updatedTemplate.getId());
        return testTemplateMapper.toTestTemplateResponse(updatedTemplate);
    }

    @Override
    @Transactional
    public void deleteTestTemplate(Integer id) {
        if (!testTemplateRepository.existsById(id)) {
            throw new TestTemplateNotFoundException("Test template not found with id: " + id, BusinessErrorCodes.TEST_TEMPLATE_NOT_FOUND);
        }
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