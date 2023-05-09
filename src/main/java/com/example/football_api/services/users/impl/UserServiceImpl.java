package com.example.football_api.services.users.impl;

import com.example.football_api.exceptions.AppException;
import com.example.football_api.exceptions.UnauthorizedException;
import com.example.football_api.entities.users.ERole;
import com.example.football_api.entities.users.Role;
import com.example.football_api.entities.users.User;
import com.example.football_api.dto.users.request.UpdateUserRequest;
import com.example.football_api.dto.users.response.ApiResponse;
import com.example.football_api.dto.users.response.UserResponse;
import com.example.football_api.repositories.users.UserRepository;
import com.example.football_api.security.userDetails.UserDetailsImpl;
import com.example.football_api.services.users.RoleService;
import com.example.football_api.services.users.UserService;
import com.example.football_api.services.users.mappers.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public UserResponse getCurrentUser(UserDetailsImpl userDetails) {
            return UserResponse.builder()
                    .firstName(userDetails.getFirstName())
                    .lastName(userDetails.getLastName())
                    .email(userDetails.getEmail())
                    .build();
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return userMapper.map(findUserByEmail(email));
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest updateInfoRequest, String email, UserDetailsImpl currentUser) {
        User user = findUserByEmail(email);
        if(user.getId().equals(currentUser.getId())){
            User updatedUser = userMapper.map(user, updateInfoRequest);
            return userMapper.map(userRepository.save(updatedUser));
        }
        throw new UnauthorizedException("Can't update not your account.");
    }

    @Override
    @Transactional
    public boolean deleteUser(String email, UserDetailsImpl currentUser) {
        User user = findUserByEmail(email);
        if(user.getId().equals(currentUser.getId()) ||
                currentUser.getAuthorities().contains(new SimpleGrantedAuthority(ERole.ROLE_ADMIN.name()))){
            userRepository.delete(user);
            return true;
        }
        throw new UnauthorizedException("Can't delete not your account.");
    }

    @Override
    public ApiResponse giveModerator(String email) {
        User user = findUserByEmail(email);

        Role roleModerator = roleService.getRole(ERole.ROLE_MODERATOR);
        if(roleService.checkIfUserHasRole(user, roleModerator)){
            throw new AppException(email + " has already role " + ERole.ROLE_MODERATOR.name());
        }

        user.getRoles().add(roleModerator);
        userRepository.save(user);

        return new ApiResponse(Boolean.TRUE, "Moderator role set to user: " + email);
    }

    @Override
    public ApiResponse removeModerator(String email) {
        User user = findUserByEmail(email);

        Role roleModerator = roleService.getRole(ERole.ROLE_MODERATOR);
        if(!roleService.checkIfUserHasRole(user, roleModerator)){
            throw new AppException(email + " hasn't got role " + ERole.ROLE_MODERATOR.name());
        }

        user.getRoles().remove(roleModerator);
        userRepository.save(user);

        return new ApiResponse(Boolean.TRUE, "Moderator role removed from user: " + email);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Override
    public User findUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(String.valueOf(id)));
    }
}
