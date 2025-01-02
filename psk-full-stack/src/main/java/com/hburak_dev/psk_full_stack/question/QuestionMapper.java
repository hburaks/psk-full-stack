package com.hburak_dev.psk_full_stack.question;

import com.hburak_dev.psk_full_stack.choice.Choice;
import com.hburak_dev.psk_full_stack.choice.ChoiceMapper;
import com.hburak_dev.psk_full_stack.test.Test;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionMapper {

        private final ChoiceMapper choiceMapper;

        public PublicQuestionResponse toPublicQuestionResponse(Question question) {
                return PublicQuestionResponse.builder()
                                .id(question.getId())
                                .text(question.getText())
                                .publicChoiceResponseList(question.getChoices().stream()
                                                .map(choiceMapper::toPublicChoiceResponse)
                                                .collect(Collectors.toList()))
                                .build();
        }

        public MyQuestionResponse toMyQuestionResponse(Question question) {
                return MyQuestionResponse.builder()
                                .id(question.getId())
                                .text(question.getText())
                                .choices(question.getChoices().stream()
                                                .map(choiceMapper::toMyChoiceResponse)
                                                .collect(Collectors.toList()))
                                .build();
        }

        public Question toQuestion(PublicTestQuestionRequest publicTestQuestionRequest, Integer userId, Test test) {
                Question question = new Question();

                if (publicTestQuestionRequest.getId() == null) {
                        question.setCreatedBy(userId);
                }


                if (publicTestQuestionRequest != null) {
                        if (publicTestQuestionRequest.getText() != null) {
                                question.setText(publicTestQuestionRequest.getText());
                        }

                        if (publicTestQuestionRequest.getPublicChoiceRequestList() != null) {
                                List<Choice> choices = publicTestQuestionRequest.getPublicChoiceRequestList().stream()
                                                .<Choice>map(publicChoiceRequest -> choiceMapper.toChoice(
                                                                publicChoiceRequest,
                                                                userId))
                                                .filter(choiceRequest -> choiceRequest != null
                                                                && choiceRequest.getText() != null)
                                                .collect(Collectors.toList());

                                if (!choices.isEmpty()) {
                                        question.setChoices(choices);
                                }
                        }

                        if (publicTestQuestionRequest.getId() != null) {
                                question.setId(publicTestQuestionRequest.getId());
                        }

                        question.setTest(test);

                }

                return question;
        }

        public PublicQuestionAdminResponse toPublicQuestionAdminResponse(Question question) {
                return PublicQuestionAdminResponse.builder()
                                .id(question.getId())
                                .text(question.getText())
                                .publicChoiceResponseList(question.getChoices().stream()
                                                .map(choiceMapper::toPublicChoiceAdminResponse)
                                                .collect(Collectors.toList()))
                                .build();
        }

        public UserQuestionResponse toUserQuestionResponse(Question question) {
                return UserQuestionResponse.builder()
                                .text(question.getText())
                                .userAnswer(question.getUserAnswer())
                                .choices(question.getChoices().stream()
                                                .map(choiceMapper::toUserChoiceResponse)
                                                .collect(Collectors.toList()))
                                .build();
        }
}