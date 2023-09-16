package com.parunev.docconnect.utils.validators;

import com.parunev.docconnect.models.ConfirmationToken;
import com.parunev.docconnect.models.JwtToken;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.enums.TokenType;
import com.parunev.docconnect.models.payloads.user.login.LoginResponse;
import com.parunev.docconnect.models.payloads.user.registration.RegistrationResponse;
import com.parunev.docconnect.repositories.JwtTokenRepository;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.security.exceptions.EmailAlreadyExistsAuthenticationException;
import com.parunev.docconnect.security.exceptions.InvalidEmailTokenException;
import com.parunev.docconnect.security.exceptions.InvalidLoginException;
import com.parunev.docconnect.security.payload.AuthenticationError;
import com.parunev.docconnect.security.payload.EmailError;
import com.parunev.docconnect.utils.DCLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A utility class providing various helper methods related to user authentication and validation.
 */
@Data
@Component
@RequiredArgsConstructor
public class AuthHelpers {

    private final UserRepository userRepository;
    private final JwtTokenRepository jwTokenRepository;
    private final HttpServletRequest request;
    private final DCLogger dcLogger = new DCLogger(AuthHelpers.class);

    /**
     * Validate the provided email and proceed with the registration process if the email is unique.
     *
     * @param email The email address to be validated.
     * @return The validated email address.
     * @throws EmailAlreadyExistsAuthenticationException if the email already exists in the system.
     */
    public String validateEmailAndProceed(String email){
        if (userRepository.existsByEmail(email)){
            dcLogger.warn("Email already exists: {}", email);

            throw new EmailAlreadyExistsAuthenticationException(AuthenticationError.builder()
                    .path(request.getRequestURI())
                    .error("The email address provided is already associated with an existing account.")
                    .status(HttpStatus.BAD_REQUEST)
                    .timestamp(LocalDateTime.now())
                    .build()
            );

        } else {
            dcLogger.info("Email does not exist: {}", email);
            return email;
        }
    }

    /**
     * Capitalize the first letter of a given input string.
     *
     * @param input The input string to be capitalized.
     * @return The input string with the first letter capitalized.
     */
    public String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    /**
     * Validate a confirmation token, checking if it's expired or already confirmed.
     *
     * @param confirmationToken The confirmation token to be validated.
     * @return true if the token is valid; otherwise, false.
     * @throws InvalidEmailTokenException if the token is expired or already confirmed.
     */
    public boolean validateToken(ConfirmationToken confirmationToken) {
        dcLogger.info("Validating token: {}", confirmationToken.getToken());

        if (confirmationToken.getConfirmedAt() != null) {
            dcLogger.warn("Token already confirmed: {}", confirmationToken.getToken());
            throw new InvalidEmailTokenException(EmailError.builder()
                    .path(request.getRequestURI())
                    .error("The token provided is already confirmed.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            dcLogger.warn("Token expired: {}", confirmationToken.getToken());
            throw new InvalidEmailTokenException(EmailError.builder()
                    .path(request.getRequestURI())
                    .error("The token provided has expired.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        dcLogger.info("Token is valid: {}", confirmationToken.getToken());
        return true;
    }

    /**
     * Revoke all valid jwt tokens associated with a user.
     *
     * @param user The user for whom tokens need to be revoked.
     */
    public void revokeUserTokens(User user) {
        dcLogger.info("Revoking all tokens for user: {}", user.getEmail());
        List<JwtToken> validUserTokens = jwTokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            dcLogger.info("No valid tokens found to revoke for user: {}", user.getEmail());
            return;
        }

        validUserTokens.forEach(jwToken -> {
            jwToken.setExpired(true);
            jwToken.setRevoked(true);
        });

        jwTokenRepository.saveAll(validUserTokens);
        dcLogger.info("Revoked {} valid tokens for user: {}", validUserTokens.size(), user.getEmail());
    }

    /**
     * Save a JWT token to the token repository for a user.
     *
     * @param user        The user for whom the token is saved.
     * @param accessToken The JWT access token to be saved.
     */
    public void saveJwtTokenToRepository(User user, String accessToken) {
        dcLogger.info("Saving JWT token to repository for user: {}", user.getEmail());
        jwTokenRepository.save(createJWTokenObject(user, accessToken));
    }

    private JwtToken createJWTokenObject(User user, String accessToken) {
        return JwtToken.builder()
                .user(user)
                .token(accessToken)
                .type(TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();
    }

    public User returnUserIfPresent(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    dcLogger.warn("User not found: {}", email);
                    throw new InvalidLoginException(AuthenticationError.builder()
                            .path(request.getRequestURI())
                            .error("User not found.")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });
    }

    public LoginResponse createLoginResponse(User user,String message, String accessToken, String refreshToken,String secretImageUri, boolean mfaEnabled) {
        return LoginResponse.builder()
                .path(request.getRequestURI())
                .message(message)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .emailAddress(user.getEmail())
                .secretImageUri(secretImageUri)
                .mfaEnabled(mfaEnabled)
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    public RegistrationResponse createRegistrationResponse(String message, String emailAddress, HttpStatus status) {
        return RegistrationResponse.builder()
                .path(request.getRequestURI())
                .message(message)
                .emailAddress(emailAddress)
                .timestamp(LocalDateTime.now())
                .status(status)
                .build();
    }
}
