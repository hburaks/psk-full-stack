package com.hburak_dev.psk_full_stack.usertest;

import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserTestServiceInterface {

    UserTestResponse assignTestToUser(AssignTestRequest request, Authentication connectedUser);

    List<UserTestListResponse> getAllUserTests();

    List<UserTestListResponse> getCurrentUserTests(Authentication connectedUser);

    UserTestResponse getUserTestById(Integer id, Authentication connectedUser);

    UserTestResponse startUserTest(Integer id, Authentication connectedUser);

    UserTestResponse completeUserTest(Integer id, Authentication connectedUser);
}