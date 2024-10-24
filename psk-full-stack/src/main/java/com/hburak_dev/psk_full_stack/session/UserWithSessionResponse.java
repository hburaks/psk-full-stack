package com.hburak_dev.psk_full_stack.session;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserWithSessionResponse {

    private UserForSessionResponse userForSessionResponse;

    private SessionResponseV2 sessionResponse;

}
