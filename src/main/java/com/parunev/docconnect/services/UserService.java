package com.parunev.docconnect.services;

import com.parunev.docconnect.repositories.SpecialistRepository;
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
    private final SpecialistRepository specialistRepository;

    /**
     * Load user details by email.
     *
     * @param email The email of the user or the specialist to load.
     * @return UserDetails representing the user or the specialist.
     * @throws UsernameNotFoundException if the user or the specialist with the specified email is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (userRepository.findByEmail(email).isEmpty()){
            return specialistRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Specialist not found with email : " + email));
        } else {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));
        }
    }
}
