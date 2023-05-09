package com.example.football_api.dto.users.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Size(min = 6, max = 40)
    private String password;
    @Size(max = 50)
    private String firstName;
    @Size(max = 50)
    private String lastName;
}
