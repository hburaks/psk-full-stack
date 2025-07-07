package com.hburak_dev.psk_full_stack.usertest;

import com.hburak_dev.psk_full_stack.exception.*;
import com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes;
import com.hburak_dev.psk_full_stack.question.Question;
import com.hburak_dev.psk_full_stack.question.QuestionRepository;
import com.hburak_dev.psk_full_stack.scoring.ScoreCalculationService;
import com.hburak_dev.psk_full_stack.testtemplate.TestTemplate;
import com.hburak_dev.psk_full_stack.testtemplate.TestTemplateRepository;
import com.hburak_dev.psk_full_stack.user.User;
import com.hburak_dev.psk_full_stack.useranswer.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserTestServiceImpl implements UserTestServiceInterface {

    private final UserTestRepository userTestRepository;
    private final UserTestMapper userTestMapper;
    private final UserAnswerRepositoryService userAnswerRepositoryService;
    private final QuestionRepository questionRepository;
    private final UserAnswerMapper userAnswerMapper;
    private final TestTemplateRepository testTemplateRepository;
    private final ScoreCalculationService scoreCalculationService;

    @Override
    @Transactional
    public UserTestResponse assignTestToUser(AssignTestRequest request, Authentication connectedUser) {
        User admin = (User) connectedUser.getPrincipal();
        
        // Check if test is already assigned to this user
        Optional<UserTest> existingAssignment = userTestRepository.findByUserIdAndTestTemplateId(
                request.getUserId(), request.getTestTemplateId());
        
        if (existingAssignment.isPresent()) {
            throw new UserTestAlreadyCompletedException("Test template is already assigned to this user", 
                    BusinessErrorCodes.USER_TEST_ALREADY_COMPLETED);
        }

        UserTest userTest = UserTest.builder()
                .userId(request.getUserId())
                .testTemplateId(request.getTestTemplateId())
                .assignedAt(LocalDateTime.now())
                .assignedBy(admin.getId().longValue())
                .isCompleted(false)
                .createdBy(admin.getId())
                .build();
        
        UserTest savedUserTest = userTestRepository.save(userTest);
        log.info("Test template {} assigned to user {} by admin {}", 
                request.getTestTemplateId(), request.getUserId(), admin.getId());
        
        return userTestMapper.toUserTestResponse(savedUserTest);
    }

    @Override
    @Transactional
    public List<UserTestListResponse> getAllUserTests() {
        List<UserTest> userTests = userTestRepository.findAll();
        return userTests.stream()
                .map(userTest -> toUserTestListResponseWithDetails(userTest, true))
                .toList();
    }

    @Override
    @Transactional
    public List<UserTestListResponse> getCurrentUserTests(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        List<UserTest> userTests = userTestRepository.findByUserId(user.getId().longValue());
        return userTests.stream()
                .map(userTest -> toUserTestListResponseWithDetails(userTest, false))
                .toList();
    }

    private UserTestListResponse toUserTestListResponseWithDetails(UserTest userTest, boolean includeAdminComment) {
        UserTestListResponse response = userTestMapper.toUserTestListResponse(userTest);
        if (userTest.getIsCompleted()) {
            // Fetch test template to get scoring strategy
            TestTemplate testTemplate = testTemplateRepository.findById(userTest.getTestTemplateId().intValue())
                    .orElseThrow(() -> new TestTemplateNotFoundException("Test template not found", BusinessErrorCodes.TEST_TEMPLATE_NOT_FOUND));

            // Get user answers for score calculation
            List<UserAnswer> userAnswers = userAnswerRepositoryService.getUserAnswers(userTest.getId().longValue());

            // Convert UserAnswer to SubmitAnswerRequest for score calculation
            List<SubmitAnswerRequest> submitAnswerRequests = userAnswers.stream()
                    .map(userAnswer -> SubmitAnswerRequest.builder()
                            .questionId(userAnswer.getQuestionId().intValue())
                            .choiceId(userAnswer.getChoiceId() != null ? userAnswer.getChoiceId().intValue() : null)
                            .answerType(userAnswer.getChoice() != null ? userAnswer.getChoice().getAnswerType() : null)
                            .textAnswer(userAnswer.getTextAnswer())
                            .build())
                    .toList();

            // Calculate score using the appropriate strategy
            int score = scoreCalculationService.calculateScore(submitAnswerRequests, testTemplate.getScoringStrategy());
            response.setScore(score);

            // Only include admin comment if requested (for admin endpoints)
            if (includeAdminComment) {
                String comment = scoreCalculationService.getStrategy(String.valueOf(testTemplate.getScoringStrategy()))
                        .getComment(score, userAnswers.size()); // Pass total questions for comment context
                response.setAdminComment(comment);
            }
        }
        return response;
    }

    @Override
    @Transactional
    public UserTestResponse getUserTestById(Integer id, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        UserTest userTest = userTestRepository.findById(id)
                .orElseThrow(() -> new UserTestNotFoundException("User test not found with id: " + id, 
                        BusinessErrorCodes.USER_TEST_NOT_FOUND));
        
        // Ensure user can only access their own tests
        if (!userTest.getUserId().equals(user.getId().longValue())) {
            throw new UserTestAccessDeniedException("Access denied: This test does not belong to you", 
                    BusinessErrorCodes.USER_TEST_ACCESS_DENIED);
        }
        
        return userTestMapper.toUserTestResponse(userTest);
    }


    @Override
    @Transactional
    public SubmitTestResponse submitAndCompleteTest(Integer id, SubmitTestRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        // Validate user test access
        UserTest userTest = validateUserTestAccess(id, user);

        List<UserAnswerResponse> submittedAnswers = new ArrayList<>();

        // Process all answers in batch
        for (SubmitAnswerRequest answerRequest : request.getAnswers()) {
            answerRequest.setUserTestId(request.getUserTestId());

            // Validate question belongs to test template
            validateQuestion(answerRequest.getQuestionId(), userTest.getTestTemplateId());

            // Validate answer format
            validateAnswerFormat(answerRequest);

            Long choiceId = answerRequest.getChoiceId() != null ? answerRequest.getChoiceId().longValue() : null;
            String textAnswer = answerRequest.getTextAnswer();

            // Save answer
            UserAnswer userAnswer = userAnswerRepositoryService.saveAnswer(
                    userTest.getId().longValue(),
                    answerRequest.getQuestionId().longValue(),
                    choiceId,
                    textAnswer,
                    user.getId()
            );

            submittedAnswers.add(userAnswerMapper.toUserAnswerResponse(userAnswer));
        }

        // Mark test as completed
        userTest.setIsCompleted(true);
        userTest.setCompletedAt(LocalDateTime.now());

        UserTest completedTest = userTestRepository.save(userTest);

        log.info("User {} submitted and completed test {} with {} answers",
                user.getId(), id, request.getAnswers().size());

        return SubmitTestResponse.builder()
                .userTestId(completedTest.getId())
                .isCompleted(completedTest.getIsCompleted())
                .completedAt(completedTest.getCompletedAt())
                .submittedAnswers(submittedAnswers)
                .build();
    }

    private UserTest validateUserTestAccess(Integer userTestId, User user) {
        UserTest userTest = userTestRepository.findById(userTestId)
                .orElseThrow(() -> new UserTestNotFoundException("User test not found with id: " + userTestId, 
                        BusinessErrorCodes.USER_TEST_NOT_FOUND));

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

    private void validateQuestion(Integer questionId, Long testTemplateId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with id: " + questionId,
                        BusinessErrorCodes.QUESTION_NOT_FOUND));

        // Ensure question belongs to the test template
        if (!question.getTestTemplateId().equals(testTemplateId)) {
            throw new UserTestAccessDeniedException("Question does not belong to this test template", 
                    BusinessErrorCodes.USER_TEST_ACCESS_DENIED);
        }
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

    public Optional<UserTest> findById(Integer id) {
        return userTestRepository.findById(id);
    }

    public UserTest save(UserTest userTest) {
        return userTestRepository.save(userTest);
    }


    @Override
    @Transactional
    public void deleteUserTest(Integer userTestId, Authentication connectedUser) {
        User admin = (User) connectedUser.getPrincipal();

        UserTest userTest = userTestRepository.findById(userTestId)
                .orElseThrow(() -> new UserTestNotFoundException("User test not found with id: " + userTestId,
                        BusinessErrorCodes.USER_TEST_NOT_FOUND));

        // Check if test is completed - prevent deletion of completed tests
        if (userTest.getIsCompleted()) {
            throw new UserTestAlreadyCompletedException("Cannot delete completed test",
                    BusinessErrorCodes.USER_TEST_ALREADY_COMPLETED);
        }

        // Delete related user answers first (if any)
        List<UserAnswer> userAnswers = userAnswerRepositoryService.getUserAnswers(userTest.getId().longValue());
        for (UserAnswer answer : userAnswers) {
            userAnswerRepositoryService.deleteAnswer(answer.getId().longValue());
        }

        userTestRepository.delete(userTest);

        log.info("User test {} deleted by admin {}", userTestId, admin.getId());
    }

}