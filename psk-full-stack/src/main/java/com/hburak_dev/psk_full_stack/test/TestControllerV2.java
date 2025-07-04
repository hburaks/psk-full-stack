package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.testtemplate.TestTemplateServiceInterface;
import com.hburak_dev.psk_full_stack.testtemplate.TestTemplateResponse;
import com.hburak_dev.psk_full_stack.usertest.UserTestServiceInterface;
import com.hburak_dev.psk_full_stack.usertest.UserTestListResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Test Admin (Legacy)", description = "Legacy admin test endpoints - use /v2/admin/test-templates and /v2/admin/user-tests instead")
@Deprecated
public class TestControllerV2 {

    private final TestService testService;
    private final TestTemplateServiceInterface testTemplateService;
    private final UserTestServiceInterface userTestService;

     @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> uploadImage(@RequestParam("file") MultipartFile file,
            @RequestParam("testId") Integer testId) {
        try {
            testService.uploadImage(file, testId);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/upload-image-for-comment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> uploadImageForComment(@RequestParam("file") MultipartFile file,
            @RequestParam("commentId") Integer commentId) {
        try {
            testService.uploadImageForComment(file, commentId);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<AdminTestResponse> savePublicTestV2(@RequestBody PublicTestRequest publicTestRequest,
            Authentication connectedUser) {
        return ResponseEntity.ok(testService.savePublicTestV2(publicTestRequest, connectedUser));
    }

    @PostMapping("/update")
    public ResponseEntity<AdminTestResponse> updatePublicTestV2(@RequestBody PublicTestRequest publicTestRequest) {
        return ResponseEntity.ok(testService.updatePublicTestV2(publicTestRequest));
    }

    @PostMapping("/update-question")
    public ResponseEntity<Boolean> updatePublicTestQuestionsV2(
            @RequestBody PublicTestQuestionListRequest publicTestQuestionListRequest,
            Authentication connectedUser) {
        return ResponseEntity.ok(testService.updatePublicTestQuestionsV2(publicTestQuestionListRequest, connectedUser));
    }

    @PostMapping("/update-comment")
    public ResponseEntity<Boolean> updatePublicTestCommentsV2(
            @RequestBody PublicTestCommentListRequest publicTestCommentListRequest,
            Authentication connectedUser) {
        return ResponseEntity.ok(testService.updatePublicTestCommentsV2(publicTestCommentListRequest, connectedUser));
    }

    @PutMapping("/update-availability")
    public ResponseEntity<PublicTestAdminResponse> updatePublicTestAvailabilityV2(@RequestParam Integer testId, @RequestParam Boolean isAvailable) {
        return ResponseEntity.ok(testService.updatePublicTestAvailabilityV2(testId, isAvailable));
    }

    @GetMapping("/user-tests/{userId}")
    @Operation(summary = "Get tests assigned to user", 
               description = "DEPRECATED: Use GET /v2/admin/user-tests instead")
    @Deprecated
    public ResponseEntity<String> getAllTestsAssignedToUserV2(@PathVariable Integer userId) {
        return ResponseEntity.ok("This endpoint is deprecated. Please use GET /v2/admin/user-tests to get all user tests.");
    }

    @PostMapping("/assign-test")
    @Operation(summary = "Assign test to user", 
               description = "DEPRECATED: Use POST /v2/admin/user-tests/assign instead")
    @Deprecated
    public ResponseEntity<String> assignTestToUserV2(@RequestParam Integer testId, @RequestParam Integer userId, Authentication connectedUser) {
        return ResponseEntity.ok("This endpoint is deprecated. Please use POST /v2/admin/user-tests/assign with proper request body.");
    }

    @DeleteMapping("/remove-test-from-user")
    @Operation(summary = "Remove test from user", 
               description = "DEPRECATED: Delete the UserTest record directly")
    @Deprecated
    public ResponseEntity<String> removeTestFromUserV2(@RequestParam Integer testId) {
        return ResponseEntity.ok("This endpoint is deprecated. UserTest records should be managed through the new UserTest endpoints.");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete test", 
               description = "DEPRECATED: Use DELETE /v2/admin/test-templates/{id} instead")
    @Deprecated
    public ResponseEntity<String> deleteTestV2(@RequestParam Integer testId) {
        return ResponseEntity.ok("This endpoint is deprecated. Please use DELETE /v2/admin/test-templates/{id} to delete test templates.");
    }

    @GetMapping("/all")
    @Operation(summary = "Get all tests", 
               description = "DEPRECATED: Use GET /v2/admin/test-templates instead")
    @Deprecated
    public ResponseEntity<List<TestTemplateResponse>> getAllTest() {
        // Redirect to TestTemplate structure
        List<TestTemplateResponse> responses = testTemplateService.getAllTestTemplates();
        return ResponseEntity.ok(responses);
    }
}
