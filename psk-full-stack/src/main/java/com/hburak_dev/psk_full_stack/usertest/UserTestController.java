package com.hburak_dev.psk_full_stack.usertest;

import com.hburak_dev.psk_full_stack.useranswer.SubmitTestRequest;
import com.hburak_dev.psk_full_stack.useranswer.SubmitTestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/user-tests")
@RequiredArgsConstructor
@Tag(name = "User Test", description = "User endpoints for managing assigned tests")
public class UserTestController {

    private final UserTestServiceInterface userTestService;

    @GetMapping
    @Operation(summary = "Get current user's tests")
    public ResponseEntity<List<UserTestListResponse>> getCurrentUserTests(Authentication connectedUser) {
        List<UserTestListResponse> responses = userTestService.getCurrentUserTests(connectedUser);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific user test")
    public ResponseEntity<UserTestResponse> getUserTest(
            @PathVariable Integer id,
            Authentication connectedUser) {
        
        UserTestResponse response = userTestService.getUserTestById(id, connectedUser);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit all answers and complete test in one operation")
    public ResponseEntity<SubmitTestResponse> submitAndCompleteTest(
            @PathVariable Integer id,
            @Valid @RequestBody SubmitTestRequest request,
            Authentication connectedUser) {

        // Set the userTestId from path variable to ensure consistency
        request.setUserTestId(id);

        SubmitTestResponse response = userTestService.submitAndCompleteTest(id, request, connectedUser);
        return ResponseEntity.ok(response);
    }
}