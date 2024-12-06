package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.comment.PublicTestAnswerCommentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TestService {

    /**
     * Gets all tests for public
     */
    List<PublicTestResponse> getAllPublicTests();


    /**
     * Checks the answers of the public user and creates comment depending on the answer
     */
    PublicTestAnswerCommentResponse checkPublicTestAnswer(PublicTestAnswerRequest publicTestAnswerRequest);

    /**
     * Checks if there is any test for the user before the session
     */
    List<MyTestResponse> getAllMyTests(Authentication connectedUser);

    /**
     * Sends the answers of the test to the psychologist and gets the rest of the tests
     */
    ResponseEntity<Boolean> saveMyTestAnswer(MyAnswerRequest myAnswerRequest, Authentication connectedUser);

    /**
     * Creates public test
     */
    PublicTestAdminResponse createPublicTestV2(PublicTestRequest publicTestRequest);

    /**
     * Update public test
     */
    PublicTestAdminResponse updatePublicTestV2(PublicTestRequest publicTestRequest);

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

}
