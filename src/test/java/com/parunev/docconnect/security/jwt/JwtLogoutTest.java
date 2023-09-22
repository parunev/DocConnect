package com.parunev.docconnect.security.jwt;

import com.parunev.docconnect.models.JwtToken;
import com.parunev.docconnect.repositories.JwtTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.Mockito.*;

class JwtLogoutTest {

    @Mock
    private JwtTokenRepository jwTokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtLogout logoutService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogout() {
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);

        JwtToken storedToken = spy(new JwtToken());
        when(jwTokenRepository.findByToken(jwt)).thenReturn(Optional.of(storedToken));

        logoutService.logout(request, response, authentication);

        verify(storedToken).setExpired(true);
        verify(storedToken).setRevoked(true);
        verify(jwTokenRepository).save(storedToken);
    }

    @Test
    void testLogout_NoAuthorizationHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response, authentication);

        verify(jwTokenRepository, never()).save(any());
    }

    @Test
    void testLogout_InvalidAuthorizationHeader() {
        // with delete signature
        when(request.getHeader("Authorization")).thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");

        logoutService.logout(request, response, authentication);

        verify(jwTokenRepository, never()).save(any());
    }

    @Test
    void testLogout_TokenNotFound() {
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);

        when(jwTokenRepository.findByToken(jwt)).thenReturn(Optional.empty());

        logoutService.logout(request, response, authentication);

        verify(jwTokenRepository, never()).save(any());
    }
}
