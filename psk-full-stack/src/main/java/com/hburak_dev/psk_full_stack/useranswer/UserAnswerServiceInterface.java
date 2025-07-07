package com.hburak_dev.psk_full_stack.useranswer;

import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserAnswerServiceInterface {

    List<UserAnswerResponse> getUserTestAnswers(Integer userTestId, Authentication connectedUser);
}