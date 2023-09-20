package com.parunev.docconnect.service;

import com.parunev.docconnect.models.ConfirmationToken;
import com.parunev.docconnect.repositories.ConfirmationTokenRepository;
import com.parunev.docconnect.services.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ConfirmationTokenServiceTest {

    @Mock
    private ConfirmationTokenRepository tokenRepository;

    @InjectMocks
    private ConfirmationTokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveConfirmationToken() {
        ConfirmationToken token = new ConfirmationToken();
        token.setToken("testToken");

        when(tokenRepository.save(token)).thenReturn(token);

        tokenService.saveConfirmationToken(token);

        verify(tokenRepository).save(token);
    }

    @Test
    void testGetToken() {
        ConfirmationToken token = new ConfirmationToken();
        token.setToken("testToken");

        when(tokenRepository.findByToken("testToken")).thenReturn(Optional.of(token));

        Optional<ConfirmationToken> foundToken = tokenService.getToken("testToken");

        assertTrue(foundToken.isPresent());
        assertEquals("testToken", foundToken.get().getToken());
    }


    @Test
    void testSetConfirmedAt() {
        ConfirmationToken token = new ConfirmationToken();
        token.setToken("testToken");

        tokenService.setConfirmedAt(token.getToken());
        verify(tokenRepository).updateConfirmedAt(eq("testToken"), any(LocalDateTime.class));
    }

}
