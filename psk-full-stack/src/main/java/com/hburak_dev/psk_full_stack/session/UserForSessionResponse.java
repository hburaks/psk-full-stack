package com.hburak_dev.psk_full_stack.session;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserForSessionResponse {

    private Integer id;

    private String firstname;

    private String lastname;

}
