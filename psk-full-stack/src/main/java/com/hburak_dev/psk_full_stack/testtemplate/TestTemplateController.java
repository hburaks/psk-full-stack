package com.hburak_dev.psk_full_stack.testtemplate;

import com.hburak_dev.psk_full_stack.question.QuestionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("v2/admin/test-templates")
@RequiredArgsConstructor
@Tag(name = "Test Template Admin", description = "Admin endpoints for managing test templates")
public class TestTemplateController {

    private final TestTemplateServiceInterface testTemplateService;

    @PostMapping
    @Operation(summary = "Create a new test template")
    public ResponseEntity<TestTemplateResponse> createTestTemplate(
            @Valid @RequestBody TestTemplateCreateRequest request) {
        TestTemplateResponse response = testTemplateService.createTestTemplate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all test templates")
    public ResponseEntity<List<TestTemplateResponse>> getAllTestTemplates() {
        List<TestTemplateResponse> responses = testTemplateService.getAllTestTemplates();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get test template by ID")
    public ResponseEntity<TestTemplateResponse> getTestTemplateById(@PathVariable Integer id) {
        TestTemplateResponse response = testTemplateService.getTestTemplateById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update test template")
    public ResponseEntity<TestTemplateResponse> updateTestTemplate(
            @PathVariable Integer id,
            @Valid @RequestBody TestTemplateUpdateRequest request) {
        TestTemplateResponse response = testTemplateService.updateTestTemplate(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete test template")
    public ResponseEntity<Void> deleteTestTemplate(@PathVariable Integer id) {
        testTemplateService.deleteTestTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/questions")
    @Operation(summary = "Get test template questions", 
               description = "Get all questions for a test template ordered by order index")
    public ResponseEntity<List<QuestionResponse>> getTestTemplateQuestions(@PathVariable Integer id) {
        List<QuestionResponse> questions = testTemplateService.getTestTemplateQuestions(id);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/available-for-user/{userId}")
    @Operation(summary = "Get available test templates for user",
            description = "Get test templates that are not yet assigned to the specified user")
    public ResponseEntity<List<TestTemplateResponse>> getAvailableTestTemplatesForUser(@PathVariable Long userId) {
        List<TestTemplateResponse> responses = testTemplateService.getAvailableTestTemplatesForUser(userId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload image for test template")
    public ResponseEntity<HttpStatus> uploadImage(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("testTemplateId") Integer testTemplateId) {
        try {
            testTemplateService.uploadImage(file, testTemplateId);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/questions")
    @Operation(summary = "Update test template questions",
               description = "Update all questions and choices for a test template")
    public ResponseEntity<List<QuestionResponse>> updateTestTemplateQuestions(
            @PathVariable Integer id,
            @Valid @RequestBody List<QuestionResponse> questions,
            Authentication connectedUser) {
        List<QuestionResponse> updatedQuestions = testTemplateService.updateTestTemplateQuestions(id, questions, connectedUser);
        return ResponseEntity.ok(updatedQuestions);
    }
}