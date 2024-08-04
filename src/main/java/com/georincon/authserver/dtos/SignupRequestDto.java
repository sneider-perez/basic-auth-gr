package com.georincon.authserver.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignupRequestDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "El nombre de usuario debe contener únicamente letras y números")
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String names;

    @NotBlank
    private String surnames;

    @NotBlank
    @Min(8)
    private String password;

}
