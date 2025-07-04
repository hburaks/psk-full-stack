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
@RequestMapping("v2/admin/user-tests")
@RequiredArgsConstructor
@Tag(name = "User Test Admin")
public class UserTestAdminController {

    private final UserTestServiceInterface userTestService;

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
}