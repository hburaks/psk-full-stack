package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.choice.Choice;
import com.hburak_dev.psk_full_stack.choice.ChoiceRepository;
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

    private final QuestionRepository questionRepository;

    private final CommentRepository commentRepository;

    private final ChoiceRepository choiceRepository;


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
                .collect(Collectors.groupingBy(PublicTestAnswerQuestionRequest::getChosenAnswer, Collectors.counting()));
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
    public PublicTestAdminResponse createPublicTestV2(PublicTestRequest publicTestRequest) {
        Test test = testMapper.toTest(publicTestRequest);
        testRepository.save(test);
        return testMapper.toPublicTestAdminResponse(test);
    }

    @Override
    public PublicTestAdminResponse updatePublicTestV2(PublicTestRequest publicTestRequest) {
        Test test = testRepository.findById(publicTestRequest.getTestId())
                .orElseThrow(() -> new RuntimeException("Test bulunamadı"));

        if (publicTestRequest.getTitle() != null) {
            test.setTitle(publicTestRequest.getTitle());
        }
        if (publicTestRequest.getSubTitle() != null) {
            test.setSubTitle(publicTestRequest.getSubTitle());
        }
        if (publicTestRequest.getCover() != null) {
            test.setCover(publicTestRequest.getCover());
        }
        if (publicTestRequest.getIsActive() != null) {
            test.setIsActive(publicTestRequest.getIsActive());
        }

        if (publicTestRequest.getComments() != null) {
            publicTestRequest.getComments().forEach(comment -> {
                Comment commentToUpdate = test.getComments().stream()
                        .filter(c -> c.getId().equals(comment.getCommentId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Yorum bulunamadı: " + comment.getCommentId()));
                updateComment(commentToUpdate, comment);
            });
        }

        if (publicTestRequest.getPublicTestQuestionRequestList() != null) {
            publicTestRequest.getPublicTestQuestionRequestList().forEach(question -> {
                Question questionToUpdate = test.getQuestions().stream()
                        .filter(q -> q.getId().equals(question.getQuestionId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Soru bulunamadı: " + question.getQuestionId()));
                updateQuestion(questionToUpdate, question);
            });
        }

        return testMapper.toPublicTestAdminResponse(test);
    }

    private void updateComment(Comment commentToUpdate, PublicTestCommentRequest comment) {
        if (comment.getText() != null) {
            commentToUpdate.setText(comment.getText());
        }
        if (comment.getScore() != null) {
            commentToUpdate.setScore(comment.getScore());
        }
        if (comment.getTitle() != null) {
            commentToUpdate.setTitle(comment.getTitle());
        }
        if (comment.getCover() != null) {
            commentToUpdate.setCover(comment.getCover());
        }
        commentRepository.save(commentToUpdate);
    }

    private void updateQuestion(Question questionToUpdate, PublicTestQuestionRequest question) {
        if (question.getText() != null) {
            questionToUpdate.setText(question.getText());
        }
        if (question.getPublicChoiceRequestList() != null) {
            question.getPublicChoiceRequestList().forEach(choice -> {
                Choice choiceToUpdate = questionToUpdate.getChoices().stream()
                        .filter(c -> c.getId().equals(choice.getChoiceId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Seçenek bulunamadı: " + choice.getChoiceId()));
                updateChoice(choiceToUpdate, choice);
            });
        }
        questionRepository.save(questionToUpdate);
    }

    private void updateChoice(Choice choiceToUpdate, PublicChoiceRequest choice) {
        if (choice.getText() != null) {
            choiceToUpdate.setText(choice.getText());
        }
        if (choice.getAnswerType() != null) {
            choiceToUpdate.setAnswerType(choice.getAnswerType());
        }
        choiceRepository.save(choiceToUpdate);
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
        if (userTests.isEmpty()) {
            return userTestForAdminResponses;
        }
        for (Test test : userTests) {
            if (test.getQuestions() != null && !test.getQuestions().isEmpty()
                    && test.getQuestions().get(0).getUserAnswer() != null) {

                Map<AnswerType, Long> answerFreq = calculateAnswerFrequencyFromTest(test.getQuestions());
                UserTestForAdminResponse userTestForAdminResponse = testMapper.toUserTestForAdminResponse(test);
                userTestForAdminResponse.setAnswerDistribution(answerFreq);
                userTestForAdminResponses.add(userTestForAdminResponse);
            } else {
                userTestForAdminResponses.add(testMapper.toUserTestForAdminResponse(test));
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
                                .collect(Collectors.toList())
                )
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
}
