package com.example.football_api.controllers.users;

import com.example.football_api.email.services.ConfirmationTokenServiceImpl;
import com.example.football_api.entities.users.User;
import com.example.football_api.dto.users.request.LoginRequest;
import com.example.football_api.dto.users.request.SignUpRequest;
import com.example.football_api.dto.users.response.ApiResponse;
import com.example.football_api.dto.users.response.JwtResponse;
import com.example.football_api.dto.users.response.MessageResponse;
import com.example.football_api.services.users.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    private ConfirmationTokenServiceImpl confirmationTokenService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        final JwtResponse jwtResponse = authService.signIn(loginRequest);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if (!authService.checkEmailAvailability(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User createdUser = authService.createUser(signUpRequest);

        confirmationTokenService.sendConfirmationEmail(createdUser);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users/{email}")
                .buildAndExpand(createdUser.getEmail()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "User registered," +
                " to enable your account confirm your email in 15 min."));
    }

    @GetMapping(path = "confirm")
    public ResponseEntity<ApiResponse> confirmToken(@RequestParam("token") String token){
        return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, confirmationTokenService.confirmToken(token)));
    }

    @GetMapping(path = "confirmationEmail/{email}")
    public ResponseEntity<?> sendConfrimationEmail(@PathVariable String email){
        confirmationTokenService.sendConfirmationEmail(email);

        return ResponseEntity.ok("Email sent to: " + email);
    }

    @GetMapping("/oauth2/token")
    @ResponseStatus(HttpStatus.OK)
    public void authenticateUser() {}

}
