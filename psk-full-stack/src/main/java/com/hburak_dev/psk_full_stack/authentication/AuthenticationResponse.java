package com.hburak_dev.psk_full_stack.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String token;
}
