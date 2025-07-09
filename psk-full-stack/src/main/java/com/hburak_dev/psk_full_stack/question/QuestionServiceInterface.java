package com.hburak_dev.psk_full_stack.question;

import java.util.List;

public interface QuestionServiceInterface {

    QuestionResponse createQuestion(QuestionCreateRequest request);

    List<QuestionResponse> getQuestionsByTestTemplate(Long testTemplateId);

    QuestionResponse getQuestionById(Integer id);

    QuestionResponse updateQuestion(Integer id, QuestionUpdateRequest request);

    void deleteQuestion(Integer id);

    QuestionResponse moveQuestionUp(Integer id);

    QuestionResponse moveQuestionDown(Integer id);

    void reorderQuestions(Long testTemplateId, List<Integer> questionIds);
}