package com.hburak_dev.psk_full_stack.useranswer;

import com.hburak_dev.psk_full_stack.exception.*;
import com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes;
import com.hburak_dev.psk_full_stack.question.Question;
import com.hburak_dev.psk_full_stack.question.QuestionRepository;
import com.hburak_dev.psk_full_stack.user.User;
import com.hburak_dev.psk_full_stack.usertest.UserTest;
import com.hburak_dev.psk_full_stack.usertest.UserTestServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAnswerServiceImpl implements UserAnswerServiceInterface {

    private final UserAnswerRepositoryService userAnswerRepositoryService;
    private final UserTestServiceImpl userTestRepositoryService;
    private final QuestionRepository questionRepository;
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


    @Override
    @Transactional
    public SubmitTestResponse submitTest(SubmitTestRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        
        // Validate user test access
        UserTest userTest = validateUserTestAccess(request.getUserTestId(), user);
        
        List<UserAnswerResponse> submittedAnswers = new ArrayList<>();
        
        // Process all answers in batch
        for (SubmitAnswerRequest answerRequest : request.getAnswers()) {
            answerRequest.setUserTestId(request.getUserTestId());
            
            // Validate question belongs to test template
            validateQuestion(answerRequest.getQuestionId(), userTest.getTestTemplateId());
            
            // Validate answer format
            validateAnswerFormat(answerRequest);
            
            // Save answer
            UserAnswer userAnswer = userAnswerRepositoryService.saveAnswer(
                    userTest.getId().longValue(),
                    answerRequest.getQuestionId().longValue(),
                    answerRequest.getChoiceId() != null ? answerRequest.getChoiceId().longValue() : null,
                    answerRequest.getTextAnswer(), answerRequest.getUserTestId()
            );
            
            submittedAnswers.add(userAnswerMapper.toUserAnswerResponse(userAnswer));
        }
        
        // Mark test as completed
        userTest.setIsCompleted(true);
        userTest.setCompletedAt(LocalDateTime.now());
        
        UserTest completedTest = userTestRepositoryService.save(userTest);
        
        log.info("User {} submitted test {} with {} answers", 
                user.getId(), request.getUserTestId(), request.getAnswers().size());
        
        return SubmitTestResponse.builder()
                .userTestId(completedTest.getId())
                .isCompleted(completedTest.getIsCompleted())
                .completedAt(completedTest.getCompletedAt())
                .submittedAnswers(submittedAnswers)
                .build();
    }

    private UserTest validateUserTestAccess(Integer userTestId, User user) {
        Optional<UserTest> userTestOpt = userTestRepositoryService.findById(userTestId);
        if (userTestOpt.isEmpty()) {
            throw new UserTestNotFoundException("User test not found with id: " + userTestId, 
                    BusinessErrorCodes.USER_TEST_NOT_FOUND);
        }
        
        UserTest userTest = userTestOpt.get();
        
        // Ensure user can only access their own tests
        if (!userTest.getUserId().equals(user.getId().longValue())) {
            throw new UserTestAccessDeniedException("Access denied: This test does not belong to you", 
                    BusinessErrorCodes.USER_TEST_ACCESS_DENIED);
        }
        
        // Check if test is already completed
        if (userTest.getIsCompleted()) {
            throw new UserTestAlreadyCompletedException("Cannot submit answers: Test is already completed", 
                    BusinessErrorCodes.USER_TEST_ALREADY_COMPLETED);
        }
        
        return userTest;
    }

    private Question validateQuestion(Integer questionId, Long testTemplateId) {
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isEmpty()) {
            throw new QuestionNotFoundException("Question not found with id: " + questionId, 
                    BusinessErrorCodes.QUESTION_NOT_FOUND);
        }
        
        Question question = questionOpt.get();
        
        // Ensure question belongs to the test template
        if (!question.getTestTemplateId().equals(testTemplateId)) {
            throw new UserTestAccessDeniedException("Question does not belong to this test template", 
                    BusinessErrorCodes.USER_TEST_ACCESS_DENIED);
        }
        
        return question;
    }

    private void validateAnswerFormat(SubmitAnswerRequest request) {
        boolean hasChoice = request.getChoiceId() != null;
        boolean hasText = request.getTextAnswer() != null && !request.getTextAnswer().trim().isEmpty();
        
        if (!hasChoice && !hasText) {
            throw new InvalidAnswerFormatException("Either choiceId or textAnswer must be provided", 
                    BusinessErrorCodes.INVALID_ANSWER_FORMAT);
        }
        
        if (hasChoice && hasText) {
            throw new InvalidAnswerFormatException("Cannot provide both choiceId and textAnswer", 
                    BusinessErrorCodes.INVALID_ANSWER_FORMAT);
        }
    }
}