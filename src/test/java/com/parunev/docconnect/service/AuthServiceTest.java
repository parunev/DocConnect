package com.parunev.docconnect.service;

import com.parunev.docconnect.models.ConfirmationToken;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.UserProfile;
import com.parunev.docconnect.models.payloads.user.login.LoginRequest;
import com.parunev.docconnect.models.payloads.user.login.LoginResponse;
import com.parunev.docconnect.models.payloads.user.registration.RegistrationRequest;
import com.parunev.docconnect.repositories.PasswordTokenRepository;
import com.parunev.docconnect.repositories.UserProfileRepository;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.security.jwt.JwtService;
import com.parunev.docconnect.security.mfa.Email2FAuthentication;
import com.parunev.docconnect.security.mfa.Google2FAuthentication;
import com.parunev.docconnect.services.AuthService;
import com.parunev.docconnect.services.ConfirmationTokenService;
import com.parunev.docconnect.utils.email.EmailSender;
import com.parunev.docconnect.utils.validators.AuthHelpers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordTokenRepository passwordTokenRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private AuthHelpers authHelpers;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailSender emailSender;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Google2FAuthentication google2FAuthentication;

    @Mock
    private Email2FAuthentication email2FAuthentication;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmailAddress("test@example.com");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userProfileRepository.save(any(UserProfile.class))).thenReturn(null);

        authService.register(registrationRequest);

        verify(userRepository, times(1)).save(any(User.class));
        verify(userProfileRepository, times(1)).save(any(UserProfile.class));
        verify(confirmationTokenService, times(1)).saveConfirmationToken(any(ConfirmationToken.class));
    }

    @Test
    void testLogin() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmailAddress("test@example.com");
        loginRequest.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");
        when(authHelpers.returnUserIfPresent("test@example.com")).thenReturn(user);
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtService.generateToken(user)).thenReturn("testAccessToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("testRefreshToken");
        when(authHelpers.createLoginResponse(any(), any(), any(), any(), any(), anyBoolean()))
                .thenReturn(new LoginResponse());

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
    }
}
