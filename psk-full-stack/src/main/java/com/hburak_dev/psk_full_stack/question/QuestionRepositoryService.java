package com.hburak_dev.psk_full_stack.question;

import com.hburak_dev.psk_full_stack.choice.Choice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionRepositoryService {

    private final QuestionRepository questionRepository;

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
}