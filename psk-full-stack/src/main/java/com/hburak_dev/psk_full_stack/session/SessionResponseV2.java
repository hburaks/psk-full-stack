package com.hburak_dev.psk_full_stack.session;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SessionResponseV2 {

    private Integer sessionId;

    private UserForSessionResponse userForSessionResponse;

    private LocalDateTime date;

    private SessionStatusType sessionStatus;

    private String noteForUser;

    private String noteForPsychologist;

    private Boolean isPaid;

}
