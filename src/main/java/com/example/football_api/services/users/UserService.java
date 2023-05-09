package com.example.football_api.services.users;


import com.example.football_api.entities.users.User;
import com.example.football_api.dto.users.request.UpdateUserRequest;
import com.example.football_api.dto.users.response.ApiResponse;
import com.example.football_api.dto.users.response.UserResponse;
import com.example.football_api.security.userDetails.UserDetailsImpl;

public interface UserService {
    UserResponse getCurrentUser(UserDetailsImpl currentUser);
    UserResponse getUserByEmail(String email);
    UserResponse updateUser(UpdateUserRequest updatedUser, String username, UserDetailsImpl currentUser);
    boolean deleteUser(String username, UserDetailsImpl currentUser);
    ApiResponse giveModerator(String username);
    ApiResponse removeModerator(String username);
    User findUserByEmail(String email);
    User findUserById(Long id);
}
