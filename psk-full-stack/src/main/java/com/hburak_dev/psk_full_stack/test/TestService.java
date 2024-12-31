package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.comment.PublicTestAnswerCommentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TestService {

    /**
     * Gets all tests for public
     */
    List<PublicTestResponse> getAllPublicTests();

    /**
     * Checks the answers of the public user and creates comment depending on the
     * answer
     */
    PublicTestAnswerCommentResponse checkPublicTestAnswer(PublicTestAnswerRequest publicTestAnswerRequest);

    /**
     * Checks if there is any test for the user before the session
     */
    List<MyTestResponse> getAllMyTests(Authentication connectedUser);

    /**
     * Sends the answers of the test to the psychologist and gets the rest of the
     * tests
     */
    ResponseEntity<Boolean> saveMyTestAnswer(MyAnswerRequest myAnswerRequest, Authentication connectedUser);

    /**
     * Creates public test
     */
    AdminTestResponse savePublicTestV2(PublicTestRequest publicTestRequest, Authentication connectedUser);

    /**
     * Creates & Updates public test
     */
    AdminTestResponse updatePublicTestV2(PublicTestRequest publicTestRequest);

    /**
     * Update public test availability value
     */
    PublicTestAdminResponse updatePublicTestAvailabilityV2(Integer testId, Boolean isAvailable);

    /**
     * Admin gets all test that assigned to the user
     */
    List<UserTestForAdminResponse> getAllTestsAssignedToUserV2(Integer userId);

    /**
     * Admin assigns test to the user
     */
    ResponseEntity<Boolean> assignTestToUserV2(Integer testId, Integer userId, Authentication connectedUser);

    /**
     * Gets public test by id
     */
    PublicTestResponse getPublicTestById(Integer id);

    /**
     * Removes test from user
     */
    ResponseEntity<Boolean> removeTestFromUserV2(Integer testId);

    /**
     * Deletes test
     */
    ResponseEntity<Boolean> deleteTestV2(Integer testId);

    /**
     * Gets all tests
     */
    List<AdminTestResponse> getAllTest();

    /**
     * Uploads image for test
     */
    String uploadImage(MultipartFile file, Integer testId);

    /**
     * Updates public test question
     */
    Boolean updatePublicTestQuestionsV2(PublicTestQuestionListRequest publicTestQuestionListRequest,
            Authentication connectedUser);

    /**
     * Updates public test comment
     */
    Boolean updatePublicTestCommentsV2(PublicTestCommentListRequest publicTestCommentListRequest,
            Authentication connectedUser);

}
