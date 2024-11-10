package com.hburak_dev.psk_full_stack.choice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChoiceMapper {
    public PublicChoiceResponse toPublicChoiceResponse(Choice choice) {
        return PublicChoiceResponse.builder()
                .id(choice.getId())
                .text(choice.getText())
                .answerType(choice.getAnswerType())
                .build();
    }

    public MyChoiceResponse toMyChoiceResponse(Choice choice) {
        return MyChoiceResponse.builder()
                .choiceId(choice.getId())
                .answerType(choice.getAnswerType())
                .text(choice.getText())
                .build();
    }

    public PublicChoiceAdminResponse toPublicChoiceAdminResponse(Choice choice) {
        return PublicChoiceAdminResponse.builder()
                .id(choice.getId())
                .text(choice.getText())
                .answerType(choice.getAnswerType())
                .build();
    }

    public UserChoiceResponse toUserChoiceResponse(Choice choice) {
        return UserChoiceResponse.builder()
                .text(choice.getText())
                .answerType(choice.getAnswerType())
                .build();
    }
}