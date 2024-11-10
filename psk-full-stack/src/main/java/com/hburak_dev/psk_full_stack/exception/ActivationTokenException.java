package com.hburak_dev.psk_full_stack.exception;

import com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes;

public class ActivationTokenException extends RuntimeException {
    private final BusinessErrorCodes errorCode;

    public ActivationTokenException(String message) {
        super(message);
        this.errorCode = BusinessErrorCodes.INVALID_ACTIVATION_TOKEN;
    }

    public BusinessErrorCodes getErrorCode() {
        return errorCode;
    }
}
