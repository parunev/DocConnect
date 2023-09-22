package com.parunev.docconnect.security;

import com.parunev.docconnect.security.exceptions.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityUtilsTest {

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void resetSecurityContextHolder() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetCurrentUserDetails_WhenAuthenticatedUser() {
        UserDetails userDetails = User.withUsername("testuser").password("password").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        UserDetails currentUserDetails = SecurityUtils.getCurrentUserDetails();

        assertEquals("testuser", currentUserDetails.getUsername());
    }

    @Test
    void testGetCurrentUserDetails_WhenNoAuthentication() {
        assertThrows(UserNotFoundException.class, SecurityUtils::getCurrentUserDetails);
    }
}
