package com.hburak_dev.psk_full_stack.useranswer;

import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserAnswerServiceInterface {

    UserAnswerResponse submitAnswer(SubmitAnswerRequest request, Authentication connectedUser);

    List<UserAnswerResponse> getUserTestAnswers(Integer userTestId, Authentication connectedUser);

    UserAnswerResponse updateAnswer(Integer userTestId, Integer questionId, SubmitAnswerRequest request, Authentication connectedUser);
}