package com.hburak_dev.psk_full_stack.testtemplate;

import java.util.List;

public interface TestTemplateServiceInterface {

    TestTemplateResponse createTestTemplate(TestTemplateCreateRequest request);

    List<TestTemplateResponse> getAllTestTemplates();

    TestTemplateResponse getTestTemplateById(Integer id);

    TestTemplateResponse updateTestTemplate(Integer id, TestTemplateUpdateRequest request);

    void deleteTestTemplate(Integer id);

    List<Object> getTestTemplateQuestions(Integer id);
}