package com.hburak_dev.psk_full_stack.question;

import com.hburak_dev.psk_full_stack.choice.Choice;
import com.hburak_dev.psk_full_stack.choice.ChoiceRepository;
import com.hburak_dev.psk_full_stack.testtemplate.TestTemplateRepository;
import com.hburak_dev.psk_full_stack.exception.QuestionNotFoundException;
import com.hburak_dev.psk_full_stack.exception.TestTemplateNotFoundException;
import com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionServiceInterface {

    private final QuestionRepositoryService questionRepositoryService;
    private final QuestionMapper questionMapper;
    private final ChoiceRepository choiceRepository;
    private final TestTemplateRepository testTemplateRepository;

    @Override
    @Transactional
    public QuestionResponse createQuestion(QuestionCreateRequest request) {
        // Validate test template exists
        if (!testTemplateRepository.existsById(request.getTestTemplateId().intValue())) {
            throw new TestTemplateNotFoundException("Test template not found with id: " + request.getTestTemplateId(), 
                    BusinessErrorCodes.TEST_TEMPLATE_NOT_FOUND);
        }
        
        // Get choices if provided
        List<Choice> choices = null;
        if (request.getChoiceIds() != null && !request.getChoiceIds().isEmpty()) {
            choices = choiceRepository.findAllById(request.getChoiceIds());
        }
        
        Question question = questionRepositoryService.createQuestion(
                request.getTestTemplateId(),
                request.getText(),
                choices
        );
        
        log.info("Question created with id: {} for test template: {}", question.getId(), request.getTestTemplateId());
        return questionMapper.toQuestionResponse(question);
    }

    @Override
    @Transactional
    public List<QuestionResponse> getQuestionsByTestTemplate(Long testTemplateId) {
        List<Question> questions = questionRepositoryService.findByTestTemplateIdWithChoicesOrderByOrderIndex(testTemplateId);
        return questions.stream()
                .map(questionMapper::toQuestionResponse)
                .toList();
    }

    @Override
    @Transactional
    public QuestionResponse getQuestionById(Integer id) {
        Optional<Question> questionOpt = questionRepositoryService.findById(id);
        if (questionOpt.isEmpty()) {
            throw new QuestionNotFoundException("Question not found with id: " + id, 
                    BusinessErrorCodes.QUESTION_NOT_FOUND);
        }
        return questionMapper.toQuestionResponse(questionOpt.get());
    }

    @Override
    @Transactional
    public QuestionResponse updateQuestion(Integer id, QuestionUpdateRequest request) {
        Optional<Question> questionOpt = questionRepositoryService.findById(id);
        if (questionOpt.isEmpty()) {
            throw new QuestionNotFoundException("Question not found with id: " + id, 
                    BusinessErrorCodes.QUESTION_NOT_FOUND);
        }
        
        Question question = questionOpt.get();
        question.setText(request.getText());
        
        if (request.getOrderIndex() != null) {
            question.setOrderIndex(request.getOrderIndex());
        }
        
        // Update choices if provided
        if (request.getChoiceIds() != null) {
            List<Choice> choices = choiceRepository.findAllById(request.getChoiceIds());
            question.setChoices(choices);
        }
        
        Question savedQuestion = questionRepositoryService.save(question);
        log.info("Question updated with id: {}", id);
        return questionMapper.toQuestionResponse(savedQuestion);
    }

    @Override
    @Transactional
    public void deleteQuestion(Integer id) {
        if (!questionRepositoryService.existsById(id)) {
            throw new QuestionNotFoundException("Question not found with id: " + id, 
                    BusinessErrorCodes.QUESTION_NOT_FOUND);
        }
        questionRepositoryService.deleteQuestion(id);
        log.info("Question deleted with id: {}", id);
    }

    @Override
    @Transactional
    public QuestionResponse moveQuestionUp(Integer id) {
        Question question = questionRepositoryService.moveQuestionUp(id);
        log.info("Question moved up with id: {}", id);
        return questionMapper.toQuestionResponse(question);
    }

    @Override
    @Transactional
    public QuestionResponse moveQuestionDown(Integer id) {
        Question question = questionRepositoryService.moveQuestionDown(id);
        log.info("Question moved down with id: {}", id);
        return questionMapper.toQuestionResponse(question);
    }

    @Override
    @Transactional
    public void reorderQuestions(Long testTemplateId, List<Integer> questionIds) {
        questionRepositoryService.reorderQuestions(testTemplateId, questionIds);
        log.info("Questions reordered for test template: {}", testTemplateId);
    }
}