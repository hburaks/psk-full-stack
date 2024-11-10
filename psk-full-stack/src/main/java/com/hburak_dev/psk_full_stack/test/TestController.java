package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.comment.PublicTestAnswerCommentResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tests")
@RequiredArgsConstructor
@Tag(name = "Test")
public class TestController {

    private final TestService testService;


    @GetMapping("/public")
    public ResponseEntity<List<PublicTestResponse>> getAllPublicTests() {
        return ResponseEntity.ok(testService.getAllPublicTests());
    }

    @PostMapping("/public/check-answer")
    public ResponseEntity<PublicTestAnswerCommentResponse> checkPublicTestAnswer(@RequestBody PublicTestAnswerRequest publicTestAnswerRequest) {
        return ResponseEntity.ok(testService.checkPublicTestAnswer(publicTestAnswerRequest));
    }

    @GetMapping("/my-tests")
    public ResponseEntity<List<MyTestResponse>> getAllMyTests(Authentication connectedUser) {
        return ResponseEntity.ok(testService.getAllMyTests(connectedUser));
    }

    @PostMapping("/my-tests/save-answer")
    public ResponseEntity<Boolean> saveMyTestAnswer(@RequestBody MyAnswerRequest myAnswerRequest, Authentication connectedUser) {
        return testService.saveMyTestAnswer(myAnswerRequest, connectedUser);
    }

}