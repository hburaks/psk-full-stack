package com.hburak_dev.psk_full_stack.testtemplate;

import com.hburak_dev.psk_full_stack.exception.TestTemplateNotFoundException;
import com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v2/test-templates")
@RequiredArgsConstructor
@Tag(name = "Test Template Admin")
public class TestTemplateController {

    private final TestTemplateService testTemplateService;
    private final TestTemplateMapper testTemplateMapper;

    @PostMapping
    @Operation(summary = "Create a new test template")
    public ResponseEntity<TestTemplateResponse> createTestTemplate(
            @Valid @RequestBody TestTemplateCreateRequest request) {
        TestTemplate testTemplate = testTemplateMapper.toTestTemplate(request);
        TestTemplate savedTemplate = testTemplateService.save(testTemplate);
        TestTemplateResponse response = testTemplateMapper.toTestTemplateResponse(savedTemplate);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all test templates")
    public ResponseEntity<List<TestTemplateResponse>> getAllTestTemplates() {
        List<TestTemplate> templates = testTemplateService.findAll();
        List<TestTemplateResponse> responses = templates.stream()
                .map(testTemplateMapper::toTestTemplateResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get test template by ID")
    public ResponseEntity<TestTemplateResponse> getTestTemplateById(@PathVariable Integer id) {
        TestTemplate testTemplate = testTemplateService.findById(id)
                .orElseThrow(() -> new TestTemplateNotFoundException("Test template not found with id: " + id, BusinessErrorCodes.TEST_TEMPLATE_NOT_FOUND));
        TestTemplateResponse response = testTemplateMapper.toTestTemplateResponse(testTemplate);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update test template")
    public ResponseEntity<TestTemplateResponse> updateTestTemplate(
            @PathVariable Integer id,
            @Valid @RequestBody TestTemplateUpdateRequest request) {
        TestTemplate existingTemplate = testTemplateService.findById(id)
                .orElseThrow(() -> new TestTemplateNotFoundException("Test template not found with id: " + id, BusinessErrorCodes.TEST_TEMPLATE_NOT_FOUND));
        
        existingTemplate.setTitle(request.getTitle());
        existingTemplate.setSubTitle(request.getSubTitle());
        existingTemplate.setImageUrl(request.getImageUrl());
        existingTemplate.setIsActive(request.getIsActive());
        
        TestTemplate updatedTemplate = testTemplateService.save(existingTemplate);
        TestTemplateResponse response = testTemplateMapper.toTestTemplateResponse(updatedTemplate);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete test template")
    public ResponseEntity<Void> deleteTestTemplate(@PathVariable Integer id) {
        if (!testTemplateService.existsById(id)) {
            throw new TestTemplateNotFoundException("Test template not found with id: " + id, BusinessErrorCodes.TEST_TEMPLATE_NOT_FOUND);
        }
        testTemplateService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/questions")
    @Operation(summary = "Get test template questions")
    public ResponseEntity<List<Object>> getTestTemplateQuestions(@PathVariable Integer id) {
        if (!testTemplateService.existsById(id)) {
            throw new TestTemplateNotFoundException("Test template not found with id: " + id, BusinessErrorCodes.TEST_TEMPLATE_NOT_FOUND);
        }
        // TODO: Implement question retrieval logic when Question entity is available
        return ResponseEntity.ok(List.of());
    }
}