package com.parunev.docconnect.services;

import com.parunev.docconnect.models.ConfirmationToken;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.enums.AuthProvider;
import com.parunev.docconnect.models.enums.Role;
import com.parunev.docconnect.models.payloads.user.login.LoginRequest;
import com.parunev.docconnect.models.payloads.user.login.LoginResponse;
import com.parunev.docconnect.models.payloads.user.login.VerificationRequest;
import com.parunev.docconnect.models.payloads.user.login.VerificationResponse;
import com.parunev.docconnect.models.payloads.user.registration.RegistrationRequest;
import com.parunev.docconnect.models.payloads.user.registration.RegistrationResponse;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.security.exceptions.InvalidEmailTokenException;
import com.parunev.docconnect.security.exceptions.InvalidLoginException;
import com.parunev.docconnect.security.jwt.JwtService;
import com.parunev.docconnect.security.mfa.Email2FAuthentication;
import com.parunev.docconnect.security.mfa.Google2FAuthentication;
import com.parunev.docconnect.security.payload.AuthenticationError;
import com.parunev.docconnect.security.payload.EmailError;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.email.EmailSender;
import com.parunev.docconnect.utils.validators.AuthHelpers;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.parunev.docconnect.utils.email.Patterns.buildConfirmationEmail;

/**
 * A service class responsible for user authentication and registration.
 */
@Service
@Validated
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthHelpers authHelpers;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final AuthenticationManager authenticationManager;
    private final DCLogger dcLogger = new DCLogger(AuthService.class);
    private final Google2FAuthentication google2FAuthentication;
    private final Email2FAuthentication email2FAuthentication;
    private final ConfirmationTokenService confirmationTokenService;
    private final JwtService jwtService;

    private final static String CONFIRMATION_LINK = "http://localhost:8080/api/v1/auth/register/confirm?token=";

    /**
     * Register a new user with the provided registration request.
     *
     * @param request The registration request containing user information.
     * @return A registration response with a confirmation message.
     */
    public RegistrationResponse register(@Valid RegistrationRequest request){
        dcLogger.info("Proceeding the registration request for user: {}", request.getEmailAddress());

        User user = userRepository.save(User.builder()
                .firstName(authHelpers.capitalizeFirstLetter(request.getFirstName()))
                .lastName(authHelpers.capitalizeFirstLetter(request.getLastName()))
                .email(authHelpers.validateEmailAndProceed(request.getEmailAddress()))
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .gender(request.getGender())
                .mfaEnabled(false)
                .mfaSecret(google2FAuthentication.generateNewSecret())
                .provider(AuthProvider.LOCAL)
                .build());
        dcLogger.info("User saved to database: {}", user.getEmail());

        dcLogger.info("Generating confirmation token for user: {}", user.getEmail());
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .user(user)
                .build();

        dcLogger.info("Saving token to database for user: {}", user.getEmail());
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        dcLogger.info("Sending confirmation email to user: {}", user.getEmail());
        emailSender.send(user.getEmail(), buildConfirmationEmail(user.getName(),CONFIRMATION_LINK + confirmationToken.getToken()),
                "Welcome to DocConnect - Verify Your Email to Get Started!" + confirmationToken.getToken());

        dcLogger.info("Registration completed for email: {}", user.getEmail());

        return authHelpers.createRegistrationResponse("User registered successfully. Please check your email for confirmation link.",
                user.getEmail(),HttpStatus.CREATED);
    }

    /**
     * Log in a user with the provided login request.
     *
     * @param loginRequest The login request containing user credentials.
     * @return A login response with access and refresh tokens.
     */
    public LoginResponse login(@Valid LoginRequest loginRequest) {
        dcLogger.info("Login attempt for user: {}", loginRequest.getEmailAddress());

        User user = authHelpers.returnUserIfPresent(loginRequest.getEmailAddress());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword()));

        if (user.isMfaEnabled()){
            if (user.getMfaSecret() == null){
                user.setMfaSecret(google2FAuthentication.generateNewSecret());
                userRepository.save(user);
            }

            dcLogger.info("MFA enabled for user: {}", user.getEmail());
            return authHelpers.createLoginResponse(user, "User provided valid credentials. Please enter OTP code.",
                    null, null,
                    google2FAuthentication.generateQrCodeImageUri(user.getMfaSecret()), true);
        }

        dcLogger.info("Generating access/refresh token for user: {}", user.getEmail());
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        authHelpers.revokeUserTokens(user);
        authHelpers.saveJwtTokenToRepository(user, accessToken);

        return authHelpers.createLoginResponse(user, "User logged in successfully.",
                accessToken, refreshToken,
                null, false);
    }


    /**
     * Confirm a user's email using a confirmation token.
     *
     * @param token The confirmation token to be used for email confirmation.
     * @return A registration response with a confirmation message.
     */
    @Transactional
    public RegistrationResponse confirmToken(String token) {
        dcLogger.info("Confirming email token: {}", token);
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> {
                            dcLogger.warn("Token not found: {}", token);
                            throw new InvalidEmailTokenException(
                                    EmailError.builder()
                                            .path(authHelpers.getRequest().getRequestURI())
                                            .error("Non existing token.")
                                            .timestamp(LocalDateTime.now())
                                            .status(HttpStatus.BAD_REQUEST)
                                            .build()
                            );
                        });

        boolean isValid = authHelpers.validateToken(confirmationToken);

        if (isValid) {
            dcLogger.info("Token is valid: {}", token);
            confirmationTokenService.setConfirmedAt(token);

            dcLogger.info("ConfirmedAt timestamp has been set for token: {}", token);
            userRepository.enableAppUser(confirmationToken.getUser().getEmail());

            dcLogger.info("User enabled: {}", confirmationToken.getUser().getEmail());

            return authHelpers.createRegistrationResponse("Email confirmed successfully!",
                    confirmationToken.getUser().getEmail(), HttpStatus.OK);
        }

        return null;
    }

    /**
     * The {@code verifyLogin} method is responsible for verifying the two-factor authentication (2FA) process.
     * It takes a {@link VerificationRequest} object containing the user's email and a verification code.
     * The method checks if the provided verification code is valid for the user and, if successful, generates and returns login tokens.
     *
     * @param request A {@link VerificationRequest} object containing the user's email and verification code.
     * @return A {@link LoginResponse} object representing the result of the login verification process, including tokens if successful.
     * @throws InvalidLoginException If the provided verification code is invalid or the 2FA process fails, an exception is thrown.
     *
     * @see VerificationRequest
     * @see LoginResponse
     * @see InvalidLoginException
     */
    public LoginResponse verifyLogin(VerificationRequest request) {
        dcLogger.info("Verifying login for user: {}", request.getEmail());
        User user = authHelpers.returnUserIfPresent(request.getEmail());

        boolean isGoogle2FAValid = google2FAuthentication.isOtpValid(user.getMfaSecret(), request.getCode());
        if (isGoogle2FAValid){
            dcLogger.warn("Invalid Google OTP for user: {}", request.getEmail());
        } else if (email2FAuthentication.verifyOtp(request)) {
            dcLogger.info("OTP verified for user: {}", request.getEmail());
        } else {
            dcLogger.warn("Invalid Email OTP for user: {}", request.getEmail());
            throw new InvalidLoginException(AuthenticationError.builder()
                    .path(authHelpers.getRequest().getRequestURI())
                    .error("Invalid OTP.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        dcLogger.info("Generating access/refresh token for user: {}", user.getEmail());
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        authHelpers.revokeUserTokens(user);
        authHelpers.saveJwtTokenToRepository(user, accessToken);

        return authHelpers.createLoginResponse(
                user,
                "User logged in successfully.",
                accessToken,
                refreshToken,
                null,
                true);
    }

    public VerificationResponse sendVerificationCode(VerificationRequest verificationRequest) {
        dcLogger.info("Sending 2FA code to user: {}", verificationRequest.getEmail());
        User user = authHelpers.returnUserIfPresent(verificationRequest.getEmail());
        email2FAuthentication.sendOtp(user, "Generic Message: DocConnect 2FA");
        return VerificationResponse.builder()
                .path(authHelpers.getRequest().getRequestURI())
                .message("2FA code sent successfully!")
                .emailAddress(verificationRequest.getEmail())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }
}
