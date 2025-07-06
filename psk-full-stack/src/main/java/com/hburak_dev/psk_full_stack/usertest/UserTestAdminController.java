package com.hburak_dev.psk_full_stack.usertest;

import com.hburak_dev.psk_full_stack.useranswer.UserAnswerResponse;
import com.hburak_dev.psk_full_stack.useranswer.UserAnswerServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v2/admin/user-tests")
@RequiredArgsConstructor
@Tag(name = "User Test Admin", description = "Admin endpoints for test assignment and management")
public class UserTestAdminController {

    private final UserTestServiceInterface userTestService;
    private final UserAnswerServiceInterface userAnswerService;

    @PostMapping("/assign")
    @Operation(summary = "Assign test to user")
    public ResponseEntity<UserTestResponse> assignTest(
            @Valid @RequestBody AssignTestRequest request,
            Authentication connectedUser) {
        
        UserTestResponse response = userTestService.assignTestToUser(request, connectedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all user tests")
    public ResponseEntity<List<UserTestListResponse>> getAllUserTests() {
        List<UserTestListResponse> responses = userTestService.getAllUserTests();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{userTestId}/answers")
    @Operation(summary = "Get user's answers for a test (Admin access)")
    public ResponseEntity<List<UserAnswerResponse>> getUserTestAnswers(
            @PathVariable Integer userTestId,
            Authentication connectedUser) {

        List<UserAnswerResponse> responses = userAnswerService.getUserTestAnswers(userTestId, connectedUser);
        return ResponseEntity.ok(responses);
    }
}