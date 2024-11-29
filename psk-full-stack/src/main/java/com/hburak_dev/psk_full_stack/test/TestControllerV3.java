package com.hburak_dev.psk_full_stack.test;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hburak_dev.psk_full_stack.comment.PublicTestAnswerCommentResponse;

import java.util.List;

@RestController
@RequestMapping("v3/test")
@RequiredArgsConstructor
@Tag(name = "Test")
public class TestControllerV3 {

    private final TestService testService;

    @GetMapping()
    public ResponseEntity<List<PublicTestResponse>> getAllPublicTests() {
        return ResponseEntity.ok(testService.getAllPublicTests());
    }

    @PostMapping("/check-answer")
    public ResponseEntity<PublicTestAnswerCommentResponse> checkPublicTestAnswer(
            @RequestBody PublicTestAnswerRequest publicTestAnswerRequest) {
        return ResponseEntity.ok(testService.checkPublicTestAnswer(publicTestAnswerRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicTestResponse> getPublicTestById(@PathVariable Integer id) {
        return ResponseEntity.ok(testService.getPublicTestById(id));
    }

}
