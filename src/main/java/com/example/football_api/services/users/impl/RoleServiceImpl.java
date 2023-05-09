package com.example.football_api.services.users.impl;

import com.example.football_api.exceptions.users.RoleNotFoundException;
import com.example.football_api.entities.users.ERole;
import com.example.football_api.entities.users.Role;
import com.example.football_api.entities.users.User;
import com.example.football_api.repositories.users.RoleRepository;
import com.example.football_api.repositories.users.UserRepository;
import com.example.football_api.services.users.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    public Set<Role> getRolesForUser() {
        Set<Role> roles = new HashSet<>();
        if (userRepository.count() == 0) {
            roles.add(getRole(ERole.ROLE_USER));
            roles.add(getRole(ERole.ROLE_ADMIN));
        } else {
            roles.add(getRole(ERole.ROLE_USER));
        }

        return roles;
    }
    public Role getRole(ERole role) {
        return roleRepository
                .findByName(role)
                .orElseThrow(() -> new RoleNotFoundException("Put in DB role" + role));
    }
    @Override
    public boolean checkIfUserHasRole(User user, Role role) {
        return user.getRoles().contains(role);
    }
}
