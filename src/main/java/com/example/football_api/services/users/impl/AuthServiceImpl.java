package com.example.football_api.services.users.impl;


import com.example.football_api.entities.users.User;
import com.example.football_api.dto.users.request.LoginRequest;
import com.example.football_api.dto.users.request.SignUpRequest;
import com.example.football_api.dto.users.response.JwtResponse;
import com.example.football_api.repositories.users.UserRepository;
import com.example.football_api.security.jwt.JwtUtils;
import com.example.football_api.security.userDetails.UserDetailsImpl;
import com.example.football_api.services.users.AuthService;
import com.example.football_api.services.users.mappers.JwtMapper;
import com.example.football_api.services.users.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final JwtMapper jwtMapper;
    private final UserMapper userMapper;

    @Override
    public User createUser(SignUpRequest request) {
        User user = userMapper.newLocalUserMap(request);
        return userRepository.save(user);
    }

    @Override
    public JwtResponse signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return jwtMapper.map(jwt, userDetails, roles);
    }

    @Override
    public boolean checkEmailAvailability(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public int enableUser(String email) {
        return userRepository.enableAppUser(email);
    }
}
