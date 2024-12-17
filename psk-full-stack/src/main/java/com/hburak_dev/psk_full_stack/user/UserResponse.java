package com.hburak_dev.psk_full_stack.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {
  private Integer id;
  private String firstname;
  private String lastname;
  private String email;
  private String phoneNumber;
  private String birthYear;
}
