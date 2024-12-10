package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.choice.Choice;
import com.hburak_dev.psk_full_stack.choice.ChoiceMapper;
import com.hburak_dev.psk_full_stack.comment.*;
import com.hburak_dev.psk_full_stack.question.*;
import com.hburak_dev.psk_full_stack.user.User;
import com.hburak_dev.psk_full_stack.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    private final TestMapper testMapper;

    private final QuestionMapper questionMapper;

    private final ChoiceMapper choiceMapper;

    private final QuestionRepository questionRepository;

    @Override
    public List<PublicTestResponse> getAllPublicTests() {
        List<Test> tests = testRepository.findAllByIsActiveTrue();
        return tests.stream()
                .map(testMapper::toPublicTestResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PublicTestAnswerCommentResponse checkPublicTestAnswer(PublicTestAnswerRequest publicTestAnswerRequest) {

        Test test = testRepository.findById(publicTestAnswerRequest.getTestId())
                .orElseThrow(() -> new RuntimeException("Test bulunamadı"));

        if (!test.getIsActive()) {
            throw new RuntimeException("Test genel kullanıcıya kapalı veya aktif değil");
        }

        Map<AnswerType, Long> answerFrequency = calculateAnswerFrequency(publicTestAnswerRequest.getQuestions());

        long testScore = calculateTestScore(answerFrequency);

        Comment resultComment = findResultComment(test.getComments(), testScore);

        return commentMapper.toPublicTestAnswerCommentResponse(resultComment);
    }

    private Map<AnswerType, Long> calculateAnswerFrequency(List<PublicTestAnswerQuestionRequest> questions) {
        return questions.stream()
                .collect(
                        Collectors.groupingBy(PublicTestAnswerQuestionRequest::getChosenAnswer, Collectors.counting()));
    }

    private long calculateTestScore(Map<AnswerType, Long> answerFrequency) {
        return answerFrequency.entrySet().stream()
                .mapToLong(entry -> switch (entry.getKey()) {
                    case ANSWER_A -> 0;
                    case ANSWER_B -> 1 * entry.getValue();
                    case ANSWER_C -> 2 * entry.getValue();
                    case ANSWER_D -> 3 * entry.getValue();
                    case ANSWER_E -> 4 * entry.getValue();
                })
                .sum();
    }

    private Comment findResultComment(List<Comment> comments, long testScore) {
        return comments.stream()
                .sorted(Comparator.comparingInt(Comment::getScore))
                .filter(comment -> comment.getScore() <= testScore)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Yorum bulunamadı"));
    }

    @Override
    public List<MyTestResponse> getAllMyTests(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        List<Test> tests = user.getTests();

        return tests.stream()
                .map(testMapper::toMyTestResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Boolean> saveMyTestAnswer(MyAnswerRequest myAnswerRequest, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        Test test = testRepository.findById(myAnswerRequest.getTestId())
                .orElseThrow(() -> new RuntimeException("Test bulunamadı"));

        if (!Objects.equals(test.getUser().getId(), user.getId())) {
            throw new RuntimeException("Test kullanıcıya ait değil");
        }

        if (test.getQuestions().size() != myAnswerRequest.getQuestions().size()) {
            throw new RuntimeException("Test soru sayısı ile cevap sayısı uyuşmuyor");
        }

        List<Question> questions = test.getQuestions();

        for (MyAnswerQuestionRequest questionRequest : myAnswerRequest.getQuestions()) {
            Question question = questions.stream()
                    .filter(q -> q.getId().equals(questionRequest.getQuestionId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Soru bulunamadı"));

            question.setUserAnswer(questionRequest.getChosenAnswer());
            questionRepository.save(question);
        }

        return ResponseEntity.ok(true);
    }

    @Override
    public AdminTestResponse createPublicTestV2(PublicTestRequest publicTestRequest,
            Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        if (publicTestRequest.getTestId() != null) {
            Test testToUpdate = testRepository.findById(publicTestRequest.getTestId())
                    .orElseThrow(() -> new RuntimeException("Test bulunamadı"));

            if (publicTestRequest.getCover() != null) {
                testToUpdate.setCover(publicTestRequest.getCover());
            }
            if (publicTestRequest.getIsActive() != null) {
                testToUpdate.setIsActive(publicTestRequest.getIsActive());
            }
            if (publicTestRequest.getSubTitle() != null) {
                testToUpdate.setSubTitle(publicTestRequest.getSubTitle());
            }
            if (publicTestRequest.getTitle() != null) {
                testToUpdate.setTitle(publicTestRequest.getTitle());
            }

            if (publicTestRequest.getPublicTestQuestionRequestList() != null) {
                List<Question> existingQuestions = testToUpdate.getQuestions();
                List<Question> updatedQuestions = new ArrayList<>();

                for (PublicTestQuestionRequest questionRequest : publicTestRequest.getPublicTestQuestionRequestList()) {
                    if (questionRequest.getQuestionId() != null) {
                        Question existingQuestion = existingQuestions.stream()
                                .filter(q -> q.getId().equals(questionRequest.getQuestionId()))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Question not found"));

                        existingQuestion.setText(questionRequest.getText());
                        updateQuestionChoices(existingQuestion, questionRequest, user.getId());
                        updatedQuestions.add(existingQuestion);
                    } else {
                        Question newQuestion = questionMapper.toQuestion(questionRequest, user.getId(), testToUpdate);
                        updatedQuestions.add(newQuestion);
                    }
                }

                testToUpdate.setQuestions(updatedQuestions);
            }

            if (publicTestRequest.getComments() != null) {
                List<Comment> existingComments = testToUpdate.getComments();
                List<Comment> updatedComments = new ArrayList<>();

                for (AdminTestCommentRequest commentRequest : publicTestRequest.getComments()) {
                    if (commentRequest.getCommentId() != null) {
                        Comment existingComment = existingComments.stream()
                                .filter(c -> c.getId().equals(commentRequest.getCommentId()))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Comment not found"));

                        existingComment = commentMapper.updateComment(existingComment, commentRequest);
                        existingComment.setCreatedBy(user.getId());
                        updatedComments.add(existingComment);
                    } else {
                        Comment newComment = commentMapper.toComment(commentRequest, testToUpdate);
                        newComment.setCreatedBy(user.getId());
                        updatedComments.add(newComment);
                    }
                }

                testToUpdate.setComments(updatedComments);
            }

            testRepository.save(testToUpdate);
            return testMapper.toAdminTestResponse(testToUpdate);
        }

        Test test = testMapper.toTest(publicTestRequest, user.getId());
        testRepository.save(test);
        return testMapper.toAdminTestResponse(test);
    }

    private void updateQuestionChoices(Question question, PublicTestQuestionRequest questionRequest, Integer userId) {
        List<Choice> existingChoices = question.getChoices();
        List<Choice> updatedChoices = new ArrayList<>();

        for (PublicChoiceRequest choiceRequest : questionRequest.getPublicChoiceRequestList()) {
            if (choiceRequest.getChoiceId() != null) {
                Choice existingChoice = existingChoices.stream()
                        .filter(c -> c.getId().equals(choiceRequest.getChoiceId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Choice not found"));

                existingChoice.setText(choiceRequest.getText());
                existingChoice.setAnswerType(choiceRequest.getAnswerType());
                updatedChoices.add(existingChoice);
            } else {
                Choice newChoice = choiceMapper.toChoice(choiceRequest, userId);
                updatedChoices.add(newChoice);
            }
        }

        question.setChoices(updatedChoices);
    }

    @Override
    public PublicTestAdminResponse updatePublicTestAvailabilityV2(Integer testId, Boolean isAvailable) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test bulunamadı"));
        test.setIsActive(isAvailable);
        test = testRepository.save(test);
        return testMapper.toPublicTestAdminResponse(test);
    }

    @Override
    public List<UserTestForAdminResponse> getAllTestsAssignedToUserV2(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        List<Test> userTests = user.getTests();
        List<UserTestForAdminResponse> userTestForAdminResponses = new ArrayList<>();
        System.out.println("hbs");
        System.out.println(userTests);
        if (userTests == null) {
            return userTestForAdminResponses;
        }
        for (Test test : userTests) {
            if (test.getQuestions() != null && test.getQuestions().size() > 0) {
                if (test.getQuestions().get(0).getUserAnswer() != null) {

                Map<AnswerType, Long> answerFreq = calculateAnswerFrequencyFromTest(test.getQuestions());
                UserTestForAdminResponse userTestForAdminResponse = testMapper.toUserTestForAdminResponse(test);
                userTestForAdminResponse.setAnswerDistribution(answerFreq);
                userTestForAdminResponses.add(userTestForAdminResponse);
            } else {
                userTestForAdminResponses.add(testMapper.toUserTestForAdminResponse(test));
            }
        }
    }
        return userTestForAdminResponses;
    }

    private Map<AnswerType, Long> calculateAnswerFrequencyFromTest(List<Question> questions) {
        return questions.stream()
                .collect(Collectors.groupingBy(Question::getUserAnswer, Collectors.counting()));
    }

    @Override
    public ResponseEntity<Boolean> assignTestToUserV2(Integer testId, Integer userId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test bulunamadı"));
        User userToAssign = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        Test testForUser = createTestForUser(test, user.getId());
        testForUser.setUser(userToAssign);
        testForUser.setCreatedBy(user.getId());
        testRepository.save(testForUser);
        return ResponseEntity.ok(true);
    }

    private Test createTestForUser(Test test, Integer userId) {

        return Test.builder()
                .title(test.getTitle())
                .subTitle(test.getSubTitle())
                .cover(test.getCover())
                .isActive(false)
                .questions(
                        test.getQuestions().stream()
                                .map(question -> createQuestionForUser(question, userId))
                                .collect(Collectors.toList()))
                .comments(test.getComments().stream()
                        .map(comment -> createCommentForUser(comment, userId))
                        .collect(Collectors.toList()))
                .build();
    }

    private Question createQuestionForUser(Question question, Integer userId) {
        return Question.builder()
                .text(question.getText())
                .choices(createChoicesForUser(question.getChoices(), userId))
                .createdBy(userId)
                .build();
    }

    private List<Choice> createChoicesForUser(List<Choice> choices, Integer userId) {
        return choices.stream()
                .map(choice -> Choice.builder()
                        .text(choice.getText())
                        .answerType(choice.getAnswerType())
                        .createdBy(userId)
                        .build())
                .collect(Collectors.toList());
    }

    private Comment createCommentForUser(Comment comment, Integer userId) {
        return Comment.builder()
                .text(comment.getText())
                .score(comment.getScore())
                .createdBy(userId)
                .build();
    }

    @Override
    public PublicTestResponse getPublicTestById(Integer id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test bulunamadı"));
        return testMapper.toPublicTestResponse(test);
    }

    @Override
    public ResponseEntity<Boolean> removeTestFromUserV2(Integer testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test bulunamadı"));
        testRepository.delete(test);
        return ResponseEntity.ok(true);
    }

    @Override
    public ResponseEntity<Boolean> deleteTestV2(Integer testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test bulunamadı"));

        // Clear associations first
        test.getComments().clear();
        test.getQuestions().clear();
        testRepository.save(test); // Save to update associations

        // Now delete the test
        testRepository.delete(test);
        return ResponseEntity.ok(true);
    }

    @Override
    public List<AdminTestResponse> getAllTest() {
        List<Test> tests = testRepository.findAll();
        return tests.stream()
                .map(testMapper::toAdminTestResponse)
                .collect(Collectors.toList());
    }
}
