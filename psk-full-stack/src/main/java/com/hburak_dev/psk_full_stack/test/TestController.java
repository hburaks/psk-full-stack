package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.usertest.UserTestServiceInterface;
import com.hburak_dev.psk_full_stack.usertest.UserTestListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tests")
@RequiredArgsConstructor
@Tag(name = "Test (Legacy)", description = "Legacy test endpoints - use /v1/user-tests instead")
@Deprecated
public class TestController {

    private final UserTestServiceInterface userTestService;

    @GetMapping("/my-tests")
    @Operation(summary = "Get current user's tests", 
               description = "DEPRECATED: Use GET /v1/user-tests instead")
    @Deprecated
    public ResponseEntity<List<UserTestListResponse>> getAllMyTests(Authentication connectedUser) {
        // Redirect to new UserTest structure
        List<UserTestListResponse> responses = userTestService.getCurrentUserTests(connectedUser);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/my-tests/save-answer")
    @Operation(summary = "Save test answer", 
               description = "DEPRECATED: Use POST /v1/user-tests/{userTestId}/answers instead")
    @Deprecated
    public ResponseEntity<String> saveMyTestAnswer(@RequestBody MyAnswerRequest myAnswerRequest, Authentication connectedUser) {
        // Return deprecation notice
        return ResponseEntity.ok("This endpoint is deprecated. Please use POST /v1/user-tests/{userTestId}/answers for submitting answers.");
    }
}