package com.hburak_dev.psk_full_stack.handler;

import com.hburak_dev.psk_full_stack.exception.ActivationTokenException;
import com.hburak_dev.psk_full_stack.exception.SessionNotFoundException;
import com.hburak_dev.psk_full_stack.exception.TestTemplateNotFoundException;
import com.hburak_dev.psk_full_stack.exception.UserTestNotFoundException;
import com.hburak_dev.psk_full_stack.exception.UserTestAccessDeniedException;
import com.hburak_dev.psk_full_stack.exception.UserTestAlreadyCompletedException;
import com.hburak_dev.psk_full_stack.exception.QuestionNotFoundException;
import com.hburak_dev.psk_full_stack.exception.InvalidAnswerFormatException;
import com.hburak_dev.psk_full_stack.exception.AnswerNotFoundException;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.hburak_dev.psk_full_stack.handler.BusinessErrorCodes.*;
import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleException(LockedException exp) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(ACCOUNT_LOCKED.getCode())
                                .businessErrorDescription(ACCOUNT_LOCKED.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleException(DisabledException exp) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(ACCOUNT_DISABLED.getCode())
                                .businessErrorDescription(ACCOUNT_DISABLED.getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException() {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(BAD_CREDENTIALS.getCode())
                                .businessErrorDescription(BAD_CREDENTIALS.getDescription())
                                .error("Email veya Şifre yanlış")
                                .build()
                );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleException(MessagingException exp) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors()
                .forEach(error -> {
                    //var fieldName = ((FieldError) error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exp) {
        exp.printStackTrace();
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .businessErrorDescription("Hata oluştu, Lütfen admin ile iletişime geçin.")
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(ActivationTokenException.class)
    public ResponseEntity<ExceptionResponse> handleException(ActivationTokenException exp) {
            Set<String> validationErrors = new HashSet<>();
            validationErrors.add(exp.getMessage());
            return ResponseEntity
                            .status(INVALID_ACTIVATION_TOKEN.getHttpStatus())
                            .body(
                                            ExceptionResponse.builder()
                                                            .businessErrorCode(exp.getErrorCode().getCode())
                                                            .businessErrorDescription(
                                                                            exp.getErrorCode().getDescription())
                                                            .validationErrors(validationErrors)
                                                            .error(exp.getMessage())
                                                            .build());
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleSessionNotFoundException() {
        ExceptionResponse response = ExceptionResponse.builder()
                .businessErrorCode(SESSION_NOT_FOUND.getCode())
                .error("Session Not Found")
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(TestTemplateNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleTestTemplateNotFoundException(TestTemplateNotFoundException exp) {
        return ResponseEntity
                .status(exp.getErrorCode().getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(exp.getErrorCode().getCode())
                                .businessErrorDescription(exp.getErrorCode().getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(UserTestNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserTestNotFoundException(UserTestNotFoundException exp) {
        return ResponseEntity
                .status(exp.getErrorCode().getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(exp.getErrorCode().getCode())
                                .businessErrorDescription(exp.getErrorCode().getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(UserTestAccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleUserTestAccessDeniedException(UserTestAccessDeniedException exp) {
        return ResponseEntity
                .status(exp.getErrorCode().getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(exp.getErrorCode().getCode())
                                .businessErrorDescription(exp.getErrorCode().getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(UserTestAlreadyCompletedException.class)
    public ResponseEntity<ExceptionResponse> handleUserTestAlreadyCompletedException(UserTestAlreadyCompletedException exp) {
        return ResponseEntity
                .status(exp.getErrorCode().getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(exp.getErrorCode().getCode())
                                .businessErrorDescription(exp.getErrorCode().getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleQuestionNotFoundException(QuestionNotFoundException exp) {
        return ResponseEntity
                .status(exp.getErrorCode().getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(exp.getErrorCode().getCode())
                                .businessErrorDescription(exp.getErrorCode().getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(InvalidAnswerFormatException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidAnswerFormatException(InvalidAnswerFormatException exp) {
        return ResponseEntity
                .status(exp.getErrorCode().getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(exp.getErrorCode().getCode())
                                .businessErrorDescription(exp.getErrorCode().getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(AnswerNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleAnswerNotFoundException(AnswerNotFoundException exp) {
        return ResponseEntity
                .status(exp.getErrorCode().getHttpStatus())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(exp.getErrorCode().getCode())
                                .businessErrorDescription(exp.getErrorCode().getDescription())
                                .error(exp.getMessage())
                                .build()
                );
    }

}
