package com.hburak_dev.psk_full_stack.exception;

import com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes;
import lombok.Getter;

@Getter
public class UserTestAccessDeniedException extends RuntimeException {
    private final BusinessErrorCodes errorCode;

    public UserTestAccessDeniedException(String message, BusinessErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}