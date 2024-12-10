package com.hburak_dev.psk_full_stack.choice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.hburak_dev.psk_full_stack.test.PublicChoiceRequest;

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

    public Choice toChoice(PublicChoiceRequest publicChoiceRequest, Integer userId) {
        Choice choice = new Choice();

        if (publicChoiceRequest != null) {
            if (publicChoiceRequest.getText() != null) {
                choice.setText(publicChoiceRequest.getText());
            }

            if (publicChoiceRequest.getAnswerType() != null) {
                choice.setAnswerType(publicChoiceRequest.getAnswerType());
            } else {
                // Handle the case where answerType is null if it's required
                throw new IllegalArgumentException("AnswerType cannot be null");
            }

            if (publicChoiceRequest.getChoiceId() != null) {
                choice.setId(publicChoiceRequest.getChoiceId());
            } else {
                choice.setCreatedBy(userId);
            }
        } else {
            throw new IllegalArgumentException("PublicChoiceRequest cannot be null");
        }

        return choice;
    }
}
