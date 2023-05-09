package com.example.football_api.services.users;


import com.example.football_api.entities.users.ERole;
import com.example.football_api.entities.users.Role;
import com.example.football_api.entities.users.User;

import java.util.Set;

public interface RoleService {
    Set<Role> getRolesForUser();
    Role getRole(ERole role);
    boolean checkIfUserHasRole(User user, Role role);
}
