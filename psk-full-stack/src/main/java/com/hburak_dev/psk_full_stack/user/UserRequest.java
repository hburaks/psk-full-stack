package com.hburak_dev.psk_full_stack.user;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequest {
  private String firstname;
  private String lastname;
  @Size(min = 10, max = 10, message = "Telefon numarası '5** *** ** **' formatında olmalıdır.")private String phoneNumber;
  @Size(min = 4, max = 4, message = "Doğum yılı 4 haneli olmalıdır.")
  private String birthYear;
}