package com.example.football_api.services.users.mappers;

import com.example.football_api.dto.users.response.JwtResponse;
import com.example.football_api.security.userDetails.UserDetailsImpl;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class JwtMapper {
    public JwtResponse map(String tokenJwt, UserDetailsImpl userDetails, List<String> roles){
        return JwtResponse.builder()
                .token(tokenJwt)
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }
}
