package com.hburak_dev.psk_full_stack.usertest;

import com.hburak_dev.psk_full_stack.useranswer.SubmitTestRequest;
import com.hburak_dev.psk_full_stack.useranswer.SubmitTestResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserTestServiceInterface {

    UserTestResponse assignTestToUser(AssignTestRequest request, Authentication connectedUser);

    List<UserTestListResponse> getAllUserTests();

    List<UserTestListResponse> getCurrentUserTests(Authentication connectedUser);

    UserTestResponse getUserTestById(Integer id, Authentication connectedUser);


    SubmitTestResponse submitAndCompleteTest(Integer id, SubmitTestRequest request, Authentication connectedUser);

    void deleteUserTest(Integer userTestId, Authentication connectedUser);
}