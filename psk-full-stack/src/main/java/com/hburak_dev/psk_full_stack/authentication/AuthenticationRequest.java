package com.hburak_dev.psk_full_stack.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {

    @Email(message = "Email doğru formatta değil")
    @NotEmpty(message = "Email zorunludur")
    @NotNull(message = "Email zorunludur")
    private String email;

    @NotEmpty(message = "Şifre zorunludur")
    @NotNull(message = "Şifre zorunludur")
    @Size(min = 8, message = "Şifre en az 8 karakter uzunluğunda olmalıdır")
    private String password;
}
