package com.hburak_dev.psk_full_stack.useranswer;

import com.hburak_dev.psk_full_stack.exception.UserTestAccessDeniedException;
import com.hburak_dev.psk_full_stack.exception.UserTestAlreadyCompletedException;
import com.hburak_dev.psk_full_stack.exception.UserTestNotFoundException;
import com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes;
import com.hburak_dev.psk_full_stack.user.User;
import com.hburak_dev.psk_full_stack.usertest.UserTest;
import com.hburak_dev.psk_full_stack.usertest.UserTestServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAnswerServiceImpl implements UserAnswerServiceInterface {

    private final UserAnswerRepositoryService userAnswerRepositoryService;
    private final UserTestServiceImpl userTestRepositoryService;
    private final UserAnswerMapper userAnswerMapper;


    @Override
    @Transactional
    public List<UserAnswerResponse> getUserTestAnswers(Integer userTestId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        
        // Validate user test access
        UserTest userTest = validateUserTestAccess(userTestId, user);
        
        List<UserAnswer> answers = userAnswerRepositoryService.getUserAnswers(userTest.getId().longValue());
        
        return answers.stream()
                .map(userAnswerMapper::toUserAnswerResponse)
                .collect(Collectors.toList());
    }



    private UserTest validateUserTestAccess(Integer userTestId, User user) {
        Optional<UserTest> userTestOpt = userTestRepositoryService.findById(userTestId);
        if (userTestOpt.isEmpty()) {
            throw new UserTestNotFoundException("User test not found with id: " + userTestId, 
                    BusinessErrorCodes.USER_TEST_NOT_FOUND);
        }
        
        UserTest userTest = userTestOpt.get();

        // Allow admin to access any user's test results
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !userTest.getUserId().equals(user.getId().longValue())) {
            throw new UserTestAccessDeniedException("Access denied: This test does not belong to you and you are not an admin",
                    BusinessErrorCodes.USER_TEST_ACCESS_DENIED);
        }

        // Check if test is already completed (only for non-admin users)
        if (!isAdmin && userTest.getIsCompleted()) {
            throw new UserTestAlreadyCompletedException("Cannot submit answers: Test is already completed", 
                    BusinessErrorCodes.USER_TEST_ALREADY_COMPLETED);
        }
        
        return userTest;
    }

}