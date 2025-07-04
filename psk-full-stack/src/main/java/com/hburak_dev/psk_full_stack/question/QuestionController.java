package com.hburak_dev.psk_full_stack.question;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v2/admin/questions")
@RequiredArgsConstructor
@Tag(name = "Question Admin", description = "Admin endpoints for managing test template questions")
public class QuestionController {

    private final QuestionServiceInterface questionService;

    @PostMapping
    @Operation(summary = "Create a new question", 
               description = "Create a question for a test template")
    public ResponseEntity<QuestionResponse> createQuestion(
            @Valid @RequestBody QuestionCreateRequest request) {
        QuestionResponse response = questionService.createQuestion(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-template/{testTemplateId}")
    @Operation(summary = "Get questions by test template", 
               description = "Get all questions for a specific test template ordered by order index")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByTestTemplate(
            @PathVariable Long testTemplateId) {
        List<QuestionResponse> responses = questionService.getQuestionsByTestTemplate(testTemplateId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get question by ID", 
               description = "Get a specific question by its ID")
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable Integer id) {
        QuestionResponse response = questionService.getQuestionById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update question", 
               description = "Update an existing question")
    public ResponseEntity<QuestionResponse> updateQuestion(
            @PathVariable Integer id,
            @Valid @RequestBody QuestionUpdateRequest request) {
        QuestionResponse response = questionService.updateQuestion(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete question", 
               description = "Delete a question and reorder remaining questions")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Integer id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/move-up")
    @Operation(summary = "Move question up", 
               description = "Move question up in the order")
    public ResponseEntity<QuestionResponse> moveQuestionUp(@PathVariable Integer id) {
        QuestionResponse response = questionService.moveQuestionUp(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/move-down")
    @Operation(summary = "Move question down", 
               description = "Move question down in the order")
    public ResponseEntity<QuestionResponse> moveQuestionDown(@PathVariable Integer id) {
        QuestionResponse response = questionService.moveQuestionDown(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/test-template/{testTemplateId}/reorder")
    @Operation(summary = "Reorder questions", 
               description = "Reorder all questions for a test template")
    public ResponseEntity<Void> reorderQuestions(
            @PathVariable Long testTemplateId,
            @RequestBody List<Integer> questionIds) {
        questionService.reorderQuestions(testTemplateId, questionIds);
        return ResponseEntity.noContent().build();
    }
}