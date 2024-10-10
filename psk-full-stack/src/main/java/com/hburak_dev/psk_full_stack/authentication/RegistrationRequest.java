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
public class RegistrationRequest {

    @NotEmpty(message = "İsim girmek zorunludur.")
    @NotNull(message = "İsim girmek zorunludur.")
    private String firstname;
    @NotEmpty(message = "Soy isim girmek zorunludur.")
    @NotNull(message = "Soy isim girmek zorunludur.")
    private String lastname;
    @Email(message = "Email doğru formatta girilmedi.")
    @NotEmpty(message = "Email girmek zorunludur.")
    @NotNull(message = "Email girmek zorunludur.")
    private String email;
    @NotEmpty(message = "Şifre girmek zorunludur.")
    @NotNull(message = "Şifre girmek zorunludur.")
    @Size(min = 8, message = "Şifre minimum 8 karakter olmalıdır.")
    private String password;
    @NotEmpty(message = "Telefon numarası girmek zorunludur.")
    @NotNull(message = "Telefon numarası girmek zorunludur.")
    @Size(min = 10, max = 10, message = "Telefon numarası '5** *** ** **' formatında olmalıdır.")
    private String phoneNumber;
    @Size(min = 4, max = 4, message = "Doğum yılı 4 haneli olmalıdır.")
    private String birthYear;
}
