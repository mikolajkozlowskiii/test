package com.example.football_api.security.userDetails;

import com.example.football_api.entities.users.User;
import com.example.football_api.repositories.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findUserByEmail(email);
        return UserDetailsImpl.build(user);
    }

    @Transactional
    public UserDetails loadUserByUserId(Long id) throws UsernameNotFoundException {
        User user = findUserById(id);
        return UserDetailsImpl.build(user);
    }

    private User findUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }


    private User findUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(String.valueOf(id)));
    }
}
