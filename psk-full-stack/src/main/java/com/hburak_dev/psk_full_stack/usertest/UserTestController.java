package com.hburak_dev.psk_full_stack.usertest;

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
@Tag(name = "User Test")
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

    @PostMapping("/{id}/start")
    @Operation(summary = "Start test")
    public ResponseEntity<UserTestResponse> startTest(
            @PathVariable Integer id,
            Authentication connectedUser) {
        
        UserTestResponse response = userTestService.startUserTest(id, connectedUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete test")
    public ResponseEntity<UserTestResponse> completeTest(
            @PathVariable Integer id,
            @Valid @RequestBody CompleteTestRequest request,
            Authentication connectedUser) {
        
        UserTestResponse response = userTestService.completeUserTest(id, request, connectedUser);
        return ResponseEntity.ok(response);
    }
}