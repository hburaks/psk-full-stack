package com.hburak_dev.psk_full_stack.session;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserWithSessionResponse {

    private Integer id;

    private String firstname;

    private String lastname;

    private List<Integer> sessionIds;

}
