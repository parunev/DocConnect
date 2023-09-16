package com.parunev.docconnect.services;

import com.parunev.docconnect.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * A service class that implements Spring Security's UserDetailsService to load user details by email.
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Load user details by email.
     *
     * @param email The email of the user to load.
     * @return UserDetails representing the user.
     * @throws UsernameNotFoundException if the user with the specified email is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));
    }
}
