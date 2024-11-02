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
                .questionId(question.getId())
                .text(question.getText())
                .choices(question.getChoices().stream()
                        .map(choiceMapper::toMyChoiceResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public Question toQuestion(PublicTestQuestionRequest publicTestQuestionRequest, Test test) {
        List<Choice> choices = publicTestQuestionRequest.getPublicChoiceRequestList().stream()
                .map(publicChoiceRequest -> Choice.builder()
                        .text(publicChoiceRequest.getText())
                        .build())
                .collect(Collectors.toList());

        return Question.builder()
                .text(publicTestQuestionRequest.getText())
                .choices(choices)
                .test(test)
                .build();
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