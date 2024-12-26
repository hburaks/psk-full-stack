package com.hburak_dev.psk_full_stack.session;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SessionResponse {

    private Integer sessionId;

    private LocalDateTime date;

    private SessionStatusType sessionStatus;

    private String noteForUser;

    private String sessionLink;

}
