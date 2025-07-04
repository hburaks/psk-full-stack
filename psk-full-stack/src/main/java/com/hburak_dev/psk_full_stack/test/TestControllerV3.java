package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.testtemplate.TestTemplateServiceInterface;
import com.hburak_dev.psk_full_stack.testtemplate.TestTemplateResponse;
import com.hburak_dev.psk_full_stack.exception.TestTemplateNotFoundException;
import com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hburak_dev.psk_full_stack.comment.PublicTestAnswerCommentResponse;

import java.util.List;

@RestController
@RequestMapping("v3/test")
@RequiredArgsConstructor
@Tag(name = "Public Test", description = "Public test endpoints using TestTemplate structure")
public class TestControllerV3 {

    private final TestService testService;
    private final TestTemplateServiceInterface testTemplateService;

    @GetMapping()
    @Operation(summary = "Get all public test templates", 
               description = "Returns all active test templates available for public access")
    public ResponseEntity<List<TestTemplateResponse>> getAllPublicTests() {
        // Use TestTemplate structure for public tests
        List<TestTemplateResponse> responses = testTemplateService.getAllTestTemplates();
        // Filter only active templates for public access
        List<TestTemplateResponse> activeTemplates = responses.stream()
                .filter(template -> template.getIsActive())
                .toList();
        return ResponseEntity.ok(activeTemplates);
    }

    @PostMapping("/check-answer")
    @Operation(summary = "Check public test answer", 
               description = "Check answers for public test templates (temporary session-based)")
    public ResponseEntity<PublicTestAnswerCommentResponse> checkPublicTestAnswer(
            @RequestBody PublicTestAnswerRequest publicTestAnswerRequest) {
        // Keep existing functionality for now - this requires more complex migration
        return ResponseEntity.ok(testService.checkPublicTestAnswer(publicTestAnswerRequest));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get public test template by ID", 
               description = "Returns a specific test template for public access")
    public ResponseEntity<TestTemplateResponse> getPublicTestById(@PathVariable Integer id) {
        // Use TestTemplate structure for public test access
        TestTemplateResponse response = testTemplateService.getTestTemplateById(id);
        
        // Only return if template is active
        if (!response.getIsActive()) {
            throw new TestTemplateNotFoundException("Test template is not available for public access", 
                    BusinessErrorCodes.TEST_TEMPLATE_NOT_FOUND);
        }
        
        return ResponseEntity.ok(response);
    }
}
