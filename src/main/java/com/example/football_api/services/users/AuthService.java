package com.example.football_api.services.users;


import com.example.football_api.entities.users.User;
import com.example.football_api.dto.users.request.LoginRequest;
import com.example.football_api.dto.users.request.SignUpRequest;
import com.example.football_api.dto.users.response.JwtResponse;

public interface AuthService {
    User createUser(SignUpRequest request);
    JwtResponse signIn(LoginRequest request);
    boolean checkEmailAvailability(String email);
    int enableUser(String email);
}
