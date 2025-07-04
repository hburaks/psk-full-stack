package com.hburak_dev.psk_full_stack.exception;

import com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes;
import lombok.Getter;

@Getter
public class AnswerNotFoundException extends RuntimeException {
    private final BusinessErrorCodes errorCode;

    public AnswerNotFoundException(String message, BusinessErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}