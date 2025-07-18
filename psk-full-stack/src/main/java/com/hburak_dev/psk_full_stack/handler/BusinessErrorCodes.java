package com.hburak_dev.psk_full_stack.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Email veya Şifre yanlış"),
    INVALID_ACTIVATION_TOKEN(305, BAD_REQUEST, "Aktivasyon kodu geçersiz"),
    SESSION_STATUS_ALREADY_UPDATED(306, BAD_REQUEST, "Seans zaten bu durumda"),
    SESSION_NOT_FOUND(307, NOT_FOUND, "Seans bulunamadı"),
    TEST_TEMPLATE_NOT_FOUND(308, NOT_FOUND, "Test şablonu bulunamadı"),
    USER_TEST_NOT_FOUND(309, NOT_FOUND, "Kullanıcı testi bulunamadı"),
    USER_TEST_ACCESS_DENIED(310, FORBIDDEN, "Bu teste erişim yetkiniz bulunmamaktadır"),
    USER_TEST_ALREADY_COMPLETED(311, BAD_REQUEST, "Test zaten tamamlanmış"),
    QUESTION_NOT_FOUND(312, NOT_FOUND, "Soru bulunamadı"),
    INVALID_ANSWER_FORMAT(313, BAD_REQUEST, "Geçersiz cevap formatı"),
    ANSWER_NOT_FOUND(314, NOT_FOUND, "Cevap bulunamadı");

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}
