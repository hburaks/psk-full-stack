package com.hburak_dev.psk_full_stack.useranswer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("v1/user-tests")
@RequiredArgsConstructor
@Tag(name = "User Answer")
public class UserAnswerController {

    private final UserAnswerServiceInterface userAnswerService;

    @PostMapping("/{userTestId}/answers")
    @Operation(summary = "Submit answer to a question")
    public ResponseEntity<UserAnswerResponse> submitAnswer(
            @PathVariable Integer userTestId,
            @Valid @RequestBody SubmitAnswerRequest request,
            Authentication connectedUser) {
        
        // Set the userTestId from path variable to ensure consistency
        request.setUserTestId(userTestId);
        
        UserAnswerResponse response = userAnswerService.submitAnswer(request, connectedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userTestId}/answers")
    @Operation(summary = "Get user's answers for a test")
    public ResponseEntity<List<UserAnswerResponse>> getUserTestAnswers(
            @PathVariable Integer userTestId,
            Authentication connectedUser) {
        
        List<UserAnswerResponse> responses = userAnswerService.getUserTestAnswers(userTestId, connectedUser);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{userTestId}/answers/{questionId}")
    @Operation(summary = "Update answer for a specific question")
    public ResponseEntity<UserAnswerResponse> updateAnswer(
            @PathVariable Integer userTestId,
            @PathVariable Integer questionId,
            @Valid @RequestBody SubmitAnswerRequest request,
            Authentication connectedUser) {
        
        // Set the userTestId and questionId from path variables to ensure consistency
        request.setUserTestId(userTestId);
        request.setQuestionId(questionId);
        
        UserAnswerResponse response = userAnswerService.updateAnswer(userTestId, questionId, request, connectedUser);
        return ResponseEntity.ok(response);
    }

}