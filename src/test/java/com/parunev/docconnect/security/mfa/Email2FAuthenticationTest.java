package com.parunev.docconnect.security.mfa;

import com.google.common.cache.LoadingCache;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.payloads.user.login.VerificationRequest;
import com.parunev.docconnect.models.specialist.Specialist;
import com.parunev.docconnect.repositories.SpecialistRepository;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.security.exceptions.OtpValidationException;
import com.parunev.docconnect.utils.email.EmailSender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class Email2FAuthenticationTest {

    @Mock
    private LoadingCache<String, Integer> otpCache;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SpecialistRepository specialistRepository;

    @Mock
    private EmailSender emailSender;

    @Spy
    private Random random = new Random();

    @InjectMocks
    private Email2FAuthentication email2FAuthentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void resetCache() {
        otpCache.cleanUp();
    }


    @Test
    void testSendOtp_User() throws ExecutionException {
        User user = new User();
        user.setEmail("user@example.com");

        when(otpCache.get("user@example.com")).thenReturn(123456);
        doNothing().when(emailSender).send(anyString(), anyString(), anyString());

        email2FAuthentication.sendOtp(user, "OTP Subject");

        verify(otpCache).get("user@example.com");
        verify(otpCache).invalidate("user@example.com");
    }

    @Test
    void testSendOtp_Specialist() throws ExecutionException {
        Specialist specialist = new Specialist();
        specialist.setEmail("specialist@example.com");

        when(otpCache.get("specialist@example.com")).thenReturn(654321);
        doNothing().when(emailSender).send(anyString(), anyString(), anyString());

        email2FAuthentication.sendOtp(specialist, "OTP Subject");

        verify(otpCache).get("specialist@example.com");
        verify(otpCache).invalidate("specialist@example.com");
        verify(emailSender).send(anyString(), anyString(), anyString());
    }

    @Test
    void testSendOtp_Exception() throws ExecutionException {
        User user = new User();
        user.setEmail("user@example.com");

        when(otpCache.get("user@example.com")).thenThrow(new ExecutionException("Error", new RuntimeException()));

        assertThrows(ResponseStatusException.class, () -> email2FAuthentication.sendOtp(user, "OTP Subject"));

        verify(otpCache).get("user@example.com");
        verifyNoMoreInteractions(otpCache);
        verifyNoInteractions(emailSender);
    }

    @Test
    void testVerifyOtp_ValidOtp() throws ExecutionException {
        VerificationRequest request = new VerificationRequest();
        request.setEmail("user@example.com");
        request.setCode("123456");

        User user = new User();
        user.setEmail("user@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(java.util.Optional.of(user));
        Integer storedOneTimePassword = 123456;
        when(otpCache.get("user@example.com")).thenReturn(storedOneTimePassword);

        boolean result = email2FAuthentication.verifyOtp(request);

        assertTrue(result);
        verify(userRepository).findByEmail("user@example.com");
        verify(otpCache).get("user@example.com");
    }

    @Test
    void testVerifyOtp_InvalidOtp() throws ExecutionException {
        VerificationRequest request = new VerificationRequest();
        request.setEmail("user@example.com");
        request.setCode("654321");

        User user = new User();
        user.setEmail("user@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(java.util.Optional.of(user));
        Integer storedOneTimePassword = 123456;
        when(otpCache.get("user@example.com")).thenReturn(storedOneTimePassword);

        boolean result = email2FAuthentication.verifyOtp(request);

        assertFalse(result);
        verify(userRepository).findByEmail("user@example.com");
        verify(otpCache).get("user@example.com");
    }

    @Test
    void testVerifyOtp_UserNotFound() {
        VerificationRequest request = new VerificationRequest();
        request.setEmail("user@example.com");
        request.setCode("123456");

        when(userRepository.findByEmail("user@example.com")).thenReturn(java.util.Optional.empty());

        assertThrows(OtpValidationException.class, () -> email2FAuthentication.verifyOtp(request));

        verify(userRepository).findByEmail("user@example.com");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(otpCache);
    }
}
