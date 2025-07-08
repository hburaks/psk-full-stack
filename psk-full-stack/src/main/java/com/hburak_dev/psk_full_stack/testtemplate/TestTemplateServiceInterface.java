package com.hburak_dev.psk_full_stack.testtemplate;

import com.hburak_dev.psk_full_stack.question.QuestionResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TestTemplateServiceInterface {

    TestTemplateResponse createTestTemplate(TestTemplateCreateRequest request);

    List<TestTemplateResponse> getAllTestTemplates();

    TestTemplateResponse getTestTemplateById(Integer id);

    TestTemplateResponse updateTestTemplate(Integer id, TestTemplateUpdateRequest request);

    void deleteTestTemplate(Integer id);

    List<QuestionResponse> getTestTemplateQuestions(Integer id);

    List<TestTemplateResponse> getAvailableTestTemplatesForUser(Long userId);

    String uploadImage(MultipartFile file, Integer testTemplateId);

    List<QuestionResponse> updateTestTemplateQuestions(Integer testTemplateId, List<QuestionResponse> questions, Authentication connectedUser);
}