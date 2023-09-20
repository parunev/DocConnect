package com.parunev.docconnect.service;

import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.enums.Role;
import com.parunev.docconnect.models.specialist.Specialist;
import com.parunev.docconnect.repositories.SpecialistRepository;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SpecialistRepository specialistRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        String userEmail = "user@example.com";
        User sampleUser = User.builder()
                .email(userEmail)
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(sampleUser));

        UserDetails userDetails = userService.loadUserByUsername(userEmail);

        Set<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertEquals(sampleUser.getEmail(), userDetails.getUsername());
        assertTrue(authorities.contains("ROLE_USER"));

        verify(specialistRepository, never()).findByEmail(anyString());
    }

    @Test
    void testLoadUserByUsername_SpecialistFound() {
        String specialistEmail = "specialist@example.com";
        Specialist sampleSpecialist = Specialist.builder()
                .email(specialistEmail)
                .role(Role.ROLE_SPECIALIST)
                .build();

        when(specialistRepository.findByEmail(specialistEmail)).thenReturn(Optional.of(sampleSpecialist));

        UserDetails userDetails = userService.loadUserByUsername(specialistEmail);

        Set<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertEquals(sampleSpecialist.getEmail(), userDetails.getUsername());
        assertTrue(authorities.contains("ROLE_SPECIALIST"));

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(specialistRepository, times(1)).findByEmail(specialistEmail);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String nonExistentEmail = "nonexistent@example.com";

        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());
        when(specialistRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(nonExistentEmail));

        verify(userRepository, times(1)).findByEmail(nonExistentEmail);
        verify(specialistRepository, times(1)).findByEmail(nonExistentEmail);
    }
}
