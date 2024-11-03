package com.hburak_dev.psk_full_stack.test;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v2/test")
@RequiredArgsConstructor
@Tag(name = "Test")
public class TestControllerV2 {

    private final TestService testService;

    @PostMapping("/create")
    public ResponseEntity<PublicTestAdminResponse> createPublicTestV2(@RequestBody PublicTestRequest publicTestRequest) {
        return ResponseEntity.ok(testService.createPublicTestV2(publicTestRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<PublicTestAdminResponse> updatePublicTestV2(@RequestBody PublicTestRequest publicTestRequest) {
        return ResponseEntity.ok(testService.updatePublicTestV2(publicTestRequest));
    }

    @PutMapping("/update-availability")
    public ResponseEntity<PublicTestAdminResponse> updatePublicTestAvailabilityV2(@RequestParam Integer testId, @RequestParam Boolean isAvailable) {
        return ResponseEntity.ok(testService.updatePublicTestAvailabilityV2(testId, isAvailable));
    }

    @GetMapping("/user-tests/{userId}")
    public ResponseEntity<List<UserTestForAdminResponse>> getAllTestsAssignedToUserV2(@PathVariable Integer userId) {
        return ResponseEntity.ok(testService.getAllTestsAssignedToUserV2(userId));
    }

    @PostMapping("/assign-test")
    public ResponseEntity<Boolean> assignTestToUserV2(@RequestParam Integer testId, @RequestParam Integer userId) {
        return testService.assignTestToUserV2(testId, userId);
    }
}
