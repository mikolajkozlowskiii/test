package com.example.football_api.dto.users.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @Email()
    @NotBlank()
    private String email;
    @NotBlank()
    @Size(min = 6, max = 40)
    private String password;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
