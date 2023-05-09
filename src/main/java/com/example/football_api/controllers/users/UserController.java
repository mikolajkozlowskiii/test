package com.example.football_api.controllers.users;

import com.example.football_api.entities.users.ERole;
import com.example.football_api.dto.users.request.UpdateUserRequest;
import com.example.football_api.dto.users.response.ApiResponse;
import com.example.football_api.dto.users.response.MessageResponse;
import com.example.football_api.dto.users.response.UserResponse;
import com.example.football_api.security.userDetails.CurrentUser;
import com.example.football_api.security.userDetails.UserDetailsImpl;
import com.example.football_api.services.users.AuthService;
import com.example.football_api.services.users.RoleService;
import com.example.football_api.services.users.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final AuthService authService;
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<UserResponse> getCurrentUser(@CurrentUser UserDetailsImpl currentUser){
        UserResponse userResponse = userService.getCurrentUser(currentUser);
        System.out.println(userResponse);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getCurrentUser(@PathVariable(value = "email") String email){
        UserResponse userResponse = userService.getUserByEmail(email);

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping("/{email}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable(value = "email") String email,
                                                          @Valid @RequestBody UpdateUserRequest updateUserRequest,
                                                          @CurrentUser UserDetailsImpl currentUser){
        if (!Objects.equals(currentUser.getEmail(), email)
                && !currentUser.getAuthorities().contains(roleService.getRole(ERole.ROLE_ADMIN))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Can't update not your account!"));
        }

        UserResponse userResponse = userService.updateUser(updateUserRequest, email, currentUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/users/{email}")
                .buildAndExpand(userResponse.getEmail())
                .toUri();

        return ResponseEntity.created(location).body(userResponse);
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "email") String email,
                                                          @CurrentUser UserDetailsImpl currentUser){
        userService.deleteUser(email, currentUser);

        return new ResponseEntity<>(new ApiResponse(Boolean.TRUE,
                "User: %s has been deleted.".formatted(email)),HttpStatus.OK);
    }

    @PutMapping("/{email}/mod")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> giveMod(@PathVariable(value = "email") String email){
        final ApiResponse apiResponse = userService.giveModerator(email);

        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{email}/mod")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> removeMode(@PathVariable(value = "email") String email){
        final ApiResponse apiResponse = userService.removeModerator(email);

        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }
}
