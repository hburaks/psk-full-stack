package com.hburak_dev.psk_full_stack.session;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserSessionRequest {

    private Integer userId;

    private LocalDateTime date;

}
