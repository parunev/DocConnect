package com.parunev.docconnect.security.mfa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class Google2FAuthenticationTest {

    @InjectMocks
    private Google2FAuthentication google2FAuthentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateNewSecret() {
        Google2FAuthentication google2FAuthentication = new Google2FAuthentication();
        String secret = google2FAuthentication.generateNewSecret();
        assertNotNull(secret);
    }

    @Test
    void testGenerateQrCodeImageUri() {
        Google2FAuthentication google2FAuthentication = new Google2FAuthentication();
        String secret = google2FAuthentication.generateNewSecret();
        String qrCodeImageUri = google2FAuthentication.generateQrCodeImageUri(secret);

        assertNotNull(qrCodeImageUri);
        assertTrue(qrCodeImageUri.startsWith("data:image/png;base64,"));
    }

    @Test
    void testIsOtpValid() {
        Google2FAuthentication google2FAuthentication = new Google2FAuthentication();
        String secret = google2FAuthentication.generateNewSecret();
        google2FAuthentication.generateQrCodeImageUri(secret);

        assertFalse(google2FAuthentication.isOtpValid(secret, "123456"));
    }
}
