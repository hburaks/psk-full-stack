package com.hburak_dev.psk_full_stack.test;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("v2/test")
@RequiredArgsConstructor
@Tag(name = "Test")
public class TestControllerV2 {

    private final TestService testService;

     @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> uploadImage(@RequestParam("file") MultipartFile file,
            @RequestParam("testId   ") Integer testId) {
        try {
            testService.uploadImage(file, testId);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<AdminTestResponse> updatePublicTestV2(@ModelAttribute PublicTestRequest publicTestRequest,
            Authentication connectedUser) {
        return ResponseEntity.ok(testService.updatePublicTestV2(publicTestRequest, connectedUser));
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
    public ResponseEntity<Boolean> assignTestToUserV2(@RequestParam Integer testId, @RequestParam Integer userId, Authentication connectedUser) {
        return testService.assignTestToUserV2(testId, userId, connectedUser);
    }

    @DeleteMapping("/remove-test-from-user")
    public ResponseEntity<Boolean> removeTestFromUserV2(@RequestParam Integer testId) {
        return testService.removeTestFromUserV2(testId);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteTestV2(@RequestParam Integer testId) {
        return testService.deleteTestV2(testId);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdminTestResponse>> getAllTest() {
        return ResponseEntity.ok(testService.getAllTest());
    }
}
