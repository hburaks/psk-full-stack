package com.hburak_dev.psk_full_stack.useranswer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAnswerRepositoryService {

    private final UserAnswerRepository userAnswerRepository;

    public List<UserAnswer> findAll() {
        return userAnswerRepository.findAll();
    }

    public Optional<UserAnswer> findById(Integer id) {
        return userAnswerRepository.findById(id);
    }

    public UserAnswer save(UserAnswer userAnswer) {
        return userAnswerRepository.save(userAnswer);
    }

    public void deleteById(Integer id) {
        userAnswerRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return userAnswerRepository.existsById(id);
    }

    public UserAnswer saveAnswer(Long userTestId, Long questionId, Long choiceId, String textAnswer) {
        if ((choiceId == null && (textAnswer == null || textAnswer.trim().isEmpty())) ||
            (choiceId != null && textAnswer != null && !textAnswer.trim().isEmpty())) {
            throw new IllegalArgumentException("Either choiceId or textAnswer must be provided, but not both");
        }

        Optional<UserAnswer> existingAnswer = userAnswerRepository.findByUserTestIdAndQuestionId(userTestId, questionId);
        
        UserAnswer userAnswer;
        if (existingAnswer.isPresent()) {
            userAnswer = existingAnswer.get();
            userAnswer.setChoiceId(choiceId);
            userAnswer.setTextAnswer(textAnswer);
            userAnswer.setAnsweredAt(LocalDateTime.now());
        } else {
            userAnswer = UserAnswer.builder()
                    .userTestId(userTestId)
                    .questionId(questionId)
                    .choiceId(choiceId)
                    .textAnswer(textAnswer)
                    .answeredAt(LocalDateTime.now())
                    .build();
        }

        return userAnswerRepository.save(userAnswer);
    }

    public UserAnswer saveMultipleChoiceAnswer(Long userTestId, Long questionId, Long choiceId) {
        return saveAnswer(userTestId, questionId, choiceId, null);
    }

    public UserAnswer saveTextAnswer(Long userTestId, Long questionId, String textAnswer) {
        return saveAnswer(userTestId, questionId, null, textAnswer);
    }

    public List<UserAnswer> getUserAnswers(Long userTestId) {
        return userAnswerRepository.findByUserTestId(userTestId);
    }

    public List<UserAnswer> getAnswersByQuestion(Long questionId) {
        return userAnswerRepository.findByQuestionId(questionId);
    }

    public Optional<UserAnswer> getAnswerByUserTestAndQuestion(Long userTestId, Long questionId) {
        return userAnswerRepository.findByUserTestIdAndQuestionId(userTestId, questionId);
    }

    public List<UserAnswer> getMultipleChoiceAnswers(Long userTestId) {
        return userAnswerRepository.findMultipleChoiceAnswersByUserTest(userTestId);
    }

    public List<UserAnswer> getTextAnswers(Long userTestId) {
        return userAnswerRepository.findTextAnswersByUserTest(userTestId);
    }

    public Long countAnswersByUserTest(Long userTestId) {
        return userAnswerRepository.countAnswersByUserTest(userTestId);
    }

    public List<UserAnswer> getAnswersByChoice(Long choiceId) {
        return userAnswerRepository.findByChoiceId(choiceId);
    }

    public List<UserAnswer> getAnswersByQuestionAndChoice(Long questionId, Long choiceId) {
        return userAnswerRepository.findByQuestionIdAndChoiceId(questionId, choiceId);
    }

    public void deleteAnswer(Long userTestId, Long questionId) {
        Optional<UserAnswer> answer = userAnswerRepository.findByUserTestIdAndQuestionId(userTestId, questionId);
        answer.ifPresent(userAnswer -> userAnswerRepository.delete(userAnswer));
    }

    public boolean hasAnswered(Long userTestId, Long questionId) {
        return userAnswerRepository.findByUserTestIdAndQuestionId(userTestId, questionId).isPresent();
    }
}