package com.zufar.icedlatte.security.dto;

import com.zufar.icedlatte.common.validation.email.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(

        @UniqueEmail
        @Email(message = "Email must be valid")
        @NotBlank(message = "Email should have a length between 2 and 128 characters")
        String email,

        @NotBlank(message = "Password is the mandatory attribute")
        @Size(min = 8, max = 128, message = "Password should have a length between 8 and 128 characters")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long and contain at least one letter, one digit, and may include special characters @$!%*?&")
        String password,

        @NotBlank(message = "Code is the mandatory attribute")
        String code
) {
}