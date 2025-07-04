package com.hburak_dev.psk_full_stack.exception;

import com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes;
import lombok.Getter;

@Getter
public class UserTestNotFoundException extends RuntimeException {
    private final BusinessErrorCodes errorCode;

    public UserTestNotFoundException(String message, BusinessErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}