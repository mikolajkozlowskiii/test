package com.example.football_api.dto.users.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JwtResponse {
    private String token;
    private final String type = "Bearer";
    private Long id;
    private String email;
    private List<String> roles;
}
