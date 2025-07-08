package com.hburak_dev.psk_full_stack.question;

import com.hburak_dev.psk_full_stack.choice.Choice;
import com.hburak_dev.psk_full_stack.choice.ChoiceRepository;
import com.hburak_dev.psk_full_stack.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class QuestionRepositoryService {

    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Optional<Question> findById(Integer id) {
        return questionRepository.findById(id);
    }

    public Question save(Question question) {
        return questionRepository.save(question);
    }

    public void deleteById(Integer id) {
        questionRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return questionRepository.existsById(id);
    }

    public List<Question> findByTestTemplateId(Long testTemplateId) {
        return questionRepository.findByTestTemplateId(testTemplateId);
    }

    public List<Question> findByTestTemplateIdOrderByOrderIndex(Long testTemplateId) {
        return questionRepository.findByTestTemplateIdOrderByOrderIndex(testTemplateId);
    }

    public List<Question> findByTestTemplateIdOrderByOrderIndexAndId(Long testTemplateId) {
        return questionRepository.findByTestTemplateIdOrderByOrderIndexAndId(testTemplateId);
    }

    public List<Question> findByTestTemplateIdWithChoicesOrderByOrderIndex(Long testTemplateId) {
        return questionRepository.findByTestTemplateIdWithChoicesOrderByOrderIndex(testTemplateId);
    }

    public Long countByTestTemplateId(Long testTemplateId) {
        return questionRepository.countByTestTemplateId(testTemplateId);
    }

    public Question createQuestion(Long testTemplateId, String text, List<Choice> choices) {
        Integer maxOrderIndex = questionRepository.findMaxOrderIndexByTestTemplateId(testTemplateId);
        int nextOrderIndex = (maxOrderIndex != null) ? maxOrderIndex + 1 : 0;

        Question question = Question.builder()
                .testTemplateId(testTemplateId)
                .text(text)
                .orderIndex(nextOrderIndex)
                .choices(choices)
                .build();

        return questionRepository.save(question);
    }

    @Transactional
    public void reorderQuestions(Long testTemplateId, List<Integer> questionIds) {
        List<Question> questions = questionRepository.findByTestTemplateId(testTemplateId);
        
        for (int i = 0; i < questionIds.size(); i++) {
            Integer questionId = questionIds.get(i);
            Question question = questions.stream()
                    .filter(q -> q.getId().equals(questionId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Question not found: " + questionId));
            
            question.setOrderIndex(i);
            questionRepository.save(question);
        }
    }

    public Question updateOrderIndex(Integer questionId, Integer newOrderIndex) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + questionId));
        
        question.setOrderIndex(newOrderIndex);
        return questionRepository.save(question);
    }

    public Question moveQuestionUp(Integer questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + questionId));
        
        if (question.getOrderIndex() > 0) {
            question.setOrderIndex(question.getOrderIndex() - 1);
            return questionRepository.save(question);
        }
        
        return question;
    }

    public Question moveQuestionDown(Integer questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + questionId));
        
        Long totalQuestions = questionRepository.countByTestTemplateId(question.getTestTemplateId());
        
        if (question.getOrderIndex() < totalQuestions - 1) {
            question.setOrderIndex(question.getOrderIndex() + 1);
            return questionRepository.save(question);
        }
        
        return question;
    }

    public void deleteQuestion(Integer questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + questionId));
        
        questionRepository.deleteById(questionId);
        
        // Reorder remaining questions
        List<Question> remainingQuestions = questionRepository.findByTestTemplateIdOrderByOrderIndex(question.getTestTemplateId());
        for (int i = 0; i < remainingQuestions.size(); i++) {
            Question q = remainingQuestions.get(i);
            q.setOrderIndex(i);
            questionRepository.save(q);
        }
    }

    @Transactional
    public List<Question> updateQuestionsForTestTemplate(Long testTemplateId, List<QuestionResponse> questionResponses, Authentication connectedUser) {
        // Delete existing questions for this test template
        List<Question> existingQuestions = questionRepository.findByTestTemplateId(testTemplateId);
        questionRepository.deleteAll(existingQuestions);
        
        // Get current user ID
        User currentUser = (User) connectedUser.getPrincipal();
        Integer currentUserId = currentUser.getId();
        
        List<Question> updatedQuestions = new ArrayList<>();
        
        for (int i = 0; i < questionResponses.size(); i++) {
            QuestionResponse questionResponse = questionResponses.get(i);
            
            // Create new question with createdBy set
            Question question = Question.builder()
                    .testTemplateId(testTemplateId)
                    .text(questionResponse.getText())
                    .orderIndex(i)
                    .createdBy(currentUserId)
                    .build();
            
            // Save question first to get ID
            Question savedQuestion = questionRepository.save(question);
            
            // Create choices for this question
            if (questionResponse.getChoices() != null && !questionResponse.getChoices().isEmpty()) {
                List<Choice> choices = new ArrayList<>();
                for (Choice choice : questionResponse.getChoices()) {
                    Choice newChoice = Choice.builder()
                            .text(choice.getText())
                            .answerType(choice.getAnswerType())
                            .createdBy(currentUserId)
                            .build();
                    Choice savedChoice = choiceRepository.save(newChoice);
                    choices.add(savedChoice);
                }
                savedQuestion.setChoices(choices);
                // Save question again to persist the relationship
                savedQuestion = questionRepository.save(savedQuestion);
            }
            
            updatedQuestions.add(savedQuestion);
        }
        
        return updatedQuestions;
    }
}