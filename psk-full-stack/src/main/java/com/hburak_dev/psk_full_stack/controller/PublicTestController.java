package com.hburak_dev.psk_full_stack.controller;

import com.hburak_dev.psk_full_stack.question.QuestionResponse;
import com.hburak_dev.psk_full_stack.testtemplate.TestTemplateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v3/public/tests")
@RequiredArgsConstructor
@Tag(name = "Public Test", description = "Public endpoints for accessing and submitting tests")
public class PublicTestController {

    private final PublicTestService publicTestService;

    @GetMapping
    @Operation(summary = "Get all active test templates for public access")
    public ResponseEntity<List<TestTemplateResponse>> getAllActiveTestTemplates() {
        List<TestTemplateResponse> activeTemplates = publicTestService.getAllActiveTestTemplates();
        return ResponseEntity.ok(activeTemplates);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get test template by ID for public access")
    public ResponseEntity<TestTemplateResponse> getTestTemplateById(@PathVariable Integer id) {
        TestTemplateResponse response = publicTestService.getActiveTestTemplateById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/questions")
    @Operation(summary = "Get test template questions for public access")
    public ResponseEntity<List<QuestionResponse>> getTestTemplateQuestions(@PathVariable Integer id) {
        List<QuestionResponse> questions = publicTestService.getTestTemplateQuestions(id);
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit test answers for public access")
    public ResponseEntity<PublicTestResultResponse> submitTestAnswers(
            @PathVariable Integer id,
            @Valid @RequestBody PublicTestSubmissionRequest request) {
        
        PublicTestResultResponse response = publicTestService.processTestSubmission(id, request.getAnswers());
        return ResponseEntity.ok(response);
    }
}