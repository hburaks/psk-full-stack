package com.hburak_dev.psk_full_stack.test;

import com.hburak_dev.psk_full_stack.choice.Choice;
import com.hburak_dev.psk_full_stack.choice.ChoiceMapper;
import com.hburak_dev.psk_full_stack.choice.ChoiceRepository;
import com.hburak_dev.psk_full_stack.comment.*;
import com.hburak_dev.psk_full_stack.question.*;
import com.hburak_dev.psk_full_stack.service.FileStorageService;
import com.hburak_dev.psk_full_stack.user.User;
import com.hburak_dev.psk_full_stack.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    private final ChoiceRepository choiceRepository;

    private final TestMapper testMapper;

    private final ChoiceMapper choiceMapper;

    private final CommentRepository commentRepository;

    private final QuestionRepository questionRepository;

    private final FileStorageService fileStorageService;

    private final QuestionMapper questionMapper;

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

        List<Test> tests = testRepository.findAllByUserIdAndIsCompletedIsFalse(user.getId());
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
                    .filter(q -> q.getId().equals(questionRequest.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Soru bulunamadı"));

            question.setUserAnswer(questionRequest.getChosenAnswer());
            questionRepository.save(question);
        }

        test.setIsCompleted(true);
        testRepository.save(test);

        return ResponseEntity.ok(true);
    }

    @Override
    public AdminTestResponse savePublicTestV2(PublicTestRequest publicTestRequest, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Test test = testMapper.toTest(publicTestRequest, user.getId());
        testRepository.save(test);
        return testMapper.toAdminTestResponse(test);
    }

    @Override
    public AdminTestResponse updatePublicTestV2(PublicTestRequest publicTestRequest) {

        Test testToUpdate = testRepository.findById(publicTestRequest.getTestId())
                .orElseThrow(() -> new RuntimeException("Test bulunamadı"));

        if (publicTestRequest.getIsActive() != null) {
            testToUpdate.setIsActive(publicTestRequest.getIsActive());
        }
        if (publicTestRequest.getSubTitle() != null) {
            testToUpdate.setSubTitle(publicTestRequest.getSubTitle());
        }
        if (publicTestRequest.getTitle() != null) {
            testToUpdate.setTitle(publicTestRequest.getTitle());
        }

        testRepository.save(testToUpdate);
        return testMapper.toAdminTestResponse(testToUpdate);
    }

    @Override
    @Transactional
    public Boolean updatePublicTestCommentsV2(PublicTestCommentListRequest publicTestCommentListRequest,
            Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();

        Test testToUpdate = testRepository.findById(publicTestCommentListRequest.getTestId())
                .orElseThrow(() -> new RuntimeException("Test bulunamadı"));

        List<Comment> existingComments = testToUpdate.getComments();
        List<Comment> updatedComments = new ArrayList<>();

        List<Comment> commentsToDelete = existingComments.stream()
                .filter(c -> !publicTestCommentListRequest.getComments().stream()
                        .anyMatch(cr -> cr.getCommentId().equals(c.getId())))
                .collect(Collectors.toList());

        for (Comment comment : commentsToDelete) {
            commentRepository.delete(comment);
        }

        existingComments.removeAll(commentsToDelete);

        for (AdminTestCommentRequest commentRequest : publicTestCommentListRequest.getComments()) {
            if (commentRequest.getCommentId() != null) {
                Comment existingComment = existingComments.stream()
                        .filter(c -> c.getId().equals(commentRequest.getCommentId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Comment not found"));

                existingComment = commentMapper.updateComment(existingComment, commentRequest);
                existingComment.setCreatedBy(user.getId());

                updatedComments.add(existingComment);
                commentRepository.save(existingComment);
            } else {
                Comment newComment = commentMapper.toComment(commentRequest, testToUpdate);
                newComment.setCreatedBy(user.getId());
                updatedComments.add(newComment);
                commentRepository.save(newComment);
            }

        }

        testToUpdate.setComments(updatedComments);
        testRepository.save(testToUpdate);
        return true;
    }

    @Override
    public String uploadImageForComment(MultipartFile file, Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment bulunamadı"));

        return saveCommentImage(file, comment);
    }

    private String saveCommentImage(MultipartFile file, Comment comment) {
        if (comment.getImageUrl() != null) {
            fileStorageService.deleteFile(comment.getImageUrl(), "comments");
        }
        String fileName = fileStorageService.storeFile(file, "comments");
        comment.setImageUrl(fileName);
        commentRepository.save(comment);
        return fileName;
    }

    @Override
    @Transactional
    public Boolean updatePublicTestQuestionsV2(PublicTestQuestionListRequest publicTestQuestionListRequest,
            Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();

        Test testToUpdate = testRepository.findById(publicTestQuestionListRequest.getTestId())
                .orElseThrow(() -> new RuntimeException("Test bulunamadı"));

        List<Question> existingQuestions = testToUpdate.getQuestions();
        List<Question> updatedQuestions = new ArrayList<>();

        List<Question> questionsToDelete = existingQuestions.stream()
                .filter(q -> publicTestQuestionListRequest.getPublicTestQuestionRequestList().stream()
                        .noneMatch(qr -> qr.getId() != null && qr.getId().equals(q.getId())))
                .collect(Collectors.toList());

        for (Question question : questionsToDelete) {
            deleteQuestion(question);
        }
        existingQuestions.removeAll(questionsToDelete);

        for (PublicTestQuestionRequest questionRequest : publicTestQuestionListRequest
                .getPublicTestQuestionRequestList()) {
            Question existingQuestion = existingQuestions.stream()
                    .filter(q -> q.getId().equals(questionRequest.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingQuestion == null) {
                existingQuestion = questionMapper.toQuestion(questionRequest, user.getId(), testToUpdate);
            }

            if (existingQuestion.getId() != null) {
                updateQuestionChoices(existingQuestion, questionRequest, user.getId());
            }
            updatedQuestions.add(existingQuestion);

        }

        testToUpdate.getQuestions().clear();
        updatedQuestions.forEach(q -> q.setTest(testToUpdate));
        testToUpdate.getQuestions().addAll(updatedQuestions);

        testRepository.save(testToUpdate);
        return true;
    }

    private void updateQuestionChoices(Question question, PublicTestQuestionRequest questionRequest, Integer userId) {
        List<Choice> existingChoices = question.getChoices();
        List<Choice> updatedChoices = new ArrayList<>();

        Set<Integer> choicesToDelete = question.getChoices().stream()
                .map(Choice::getId)
                .filter(id -> questionRequest.getPublicChoiceRequestList().stream()
                        .noneMatch(choiceRequest -> choiceRequest.getId() != null
                                && choiceRequest.getId().equals(id)))
                .collect(Collectors.toSet());

        for (PublicChoiceRequest choiceRequest : questionRequest.getPublicChoiceRequestList()) {
            if (choiceRequest.getId() != null) {
                Choice existingChoice = existingChoices.stream()
                        .filter(c -> c.getId().equals(choiceRequest.getId()))
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

        choicesToDelete.forEach(id -> {
            Choice choice = existingChoices.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Choice not found"));
            choiceRepository.delete(choice);
        });

        question.setChoices(updatedChoices);
    }

    private void deleteQuestion(Question question) {
        try {
            choiceRepository.deleteAll(question.getChoices());
            questionRepository.delete(question);
        } catch (Exception e) {
            log.error("Error deleting question", e);
            throw new RuntimeException("Error deleting question");
        }
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
        Test newTest = Test.builder()
                .title(test.getTitle())
                .subTitle(test.getSubTitle())
                .imageUrl(test.getImageUrl())
                .isActive(false)
                .isCompleted(false)
                .questions(
                        test.getQuestions().stream()
                                .map(question -> createQuestionForUser(question, userId))
                                .collect(Collectors.toList()))
                .comments(new ArrayList<>(test.getComments()))
                .build();

        newTest.getQuestions().forEach(q -> q.setTest(newTest));

        return newTest;
    }

    private Question createQuestionForUser(Question question, Integer userId) {
        Question newQuestion = Question.builder()
                .text(question.getText())
                .choices(new ArrayList<>(question.getChoices()))
                .createdBy(userId)
                .build();

        return newQuestion;
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

        test.getComments().clear();
        test.getQuestions().clear();
        testRepository.save(test);

        testRepository.delete(test);
        return ResponseEntity.ok(true);
    }

    @Override
    public List<AdminTestResponse> getAllTest() {
        List<Test> tests = testRepository.findAllByUserIdIsNull();
        return tests.stream()
                .map(testMapper::toAdminTestResponse)
                .collect(Collectors.toList());
    }

    @Override
    public String uploadImage(MultipartFile file, Integer testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test bulunamadı"));
        if (test.getImageUrl() != null) {
            fileStorageService.deleteFile(test.getImageUrl(), "tests");
        }
        String fileName = fileStorageService.storeFile(file, "tests");
        test.setImageUrl(fileName);
        testRepository.save(test);
        return fileName;
    }

}
