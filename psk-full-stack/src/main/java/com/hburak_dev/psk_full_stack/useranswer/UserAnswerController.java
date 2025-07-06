package com.hburak_dev.psk_full_stack.useranswer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/user-tests")
@RequiredArgsConstructor
@Tag(name = "User Answer", description = "User endpoints for test-taking and answer submission")
public class UserAnswerController {

    private final UserAnswerServiceInterface userAnswerService;

    @PostMapping("/{userTestId}/submit-test")
    @Operation(summary = "Submit all answers for a test and complete it")
    public ResponseEntity<SubmitTestResponse> submitTest(
            @PathVariable Integer userTestId,
            @Valid @RequestBody SubmitTestRequest request,
            Authentication connectedUser) {
        
        // Set the userTestId from path variable to ensure consistency
        request.setUserTestId(userTestId);
        
        SubmitTestResponse response = userAnswerService.submitTest(request, connectedUser);
        return ResponseEntity.ok(response);
    }


}