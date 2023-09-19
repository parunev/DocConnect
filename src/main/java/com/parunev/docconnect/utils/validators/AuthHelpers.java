package com.parunev.docconnect.utils.validators;

import com.parunev.docconnect.models.*;
import com.parunev.docconnect.models.enums.TokenType;
import com.parunev.docconnect.models.payloads.city.CityResponse;
import com.parunev.docconnect.models.payloads.country.CountryResponse;
import com.parunev.docconnect.models.payloads.specialty.SpecialtyResponse;
import com.parunev.docconnect.models.payloads.user.login.ForgotPasswordResponse;
import com.parunev.docconnect.models.payloads.user.login.LoginResponse;
import com.parunev.docconnect.models.payloads.user.login.VerificationResponse;
import com.parunev.docconnect.models.payloads.user.registration.RegistrationResponse;
import com.parunev.docconnect.models.specialist.Specialist;
import com.parunev.docconnect.models.specialist.SpecialistAddress;
import com.parunev.docconnect.repositories.*;
import com.parunev.docconnect.security.exceptions.*;
import com.parunev.docconnect.security.payload.AuthenticationError;
import com.parunev.docconnect.security.payload.EmailError;
import com.parunev.docconnect.utils.DCLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A utility class providing various helper methods related to user authentication and validation.
 */
@Data
@Component
@RequiredArgsConstructor
public class AuthHelpers {

    private final UserRepository userRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final CityRepository cityRepository;
    private final SpecialtyRepository specialtyRepository;
    private final CountryRepository countryRepository;
    private final SpecialistRepository specialistRepository;
    private final SpecialistAddressRepository specialistAddressRepository;
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
        if (userRepository.existsByEmail(email) || specialistRepository.existsByEmail(email)){
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
     * Revoke all valid jwt tokens associated with a user/specialist.
     *
     * @param obj The user/specialist for whom tokens need to be revoked.
     */
    public void revokeUserTokens(Object obj) {
        String email;
        Long id;
        if (obj instanceof User user){
            id = user.getId();
            email = user.getEmail();
        } else {
            id = ((Specialist) obj).getId();
            email = ((Specialist) obj).getEmail();
        }

        dcLogger.info("Revoking all tokens for user: {}", email);
        List<JwtToken> validUserTokens = jwTokenRepository.findAllValidTokensByUser(id);
        if (validUserTokens.isEmpty()) {
            dcLogger.info("No valid tokens found to revoke for user/specialist: {}", email);
            return;
        }

        validUserTokens.forEach(jwToken -> {
            jwToken.setExpired(true);
            jwToken.setRevoked(true);
        });

        jwTokenRepository.saveAll(validUserTokens);
        dcLogger.info("Revoked {} valid tokens for user/specialist: {}", validUserTokens.size(), email);
    }

    /**
     * Save a JWT token to the token repository for a user/specialist.
     *
     * @param obj        The user/specialist for whom the token is saved.
     * @param accessToken The JWT access token to be saved.
     */
    public void saveJwtTokenToRepository(Object obj, String accessToken) {
        User user;
        Specialist specialist;
        if (obj instanceof User user1){
            user = user1;
            dcLogger.info("Saving JWT token to repository for specialist: {}", user.getEmail());
            jwTokenRepository.save(createJWTokenObject(user, accessToken));
        } else {
            specialist = (Specialist) obj;
            dcLogger.info("Saving JWT token to repository for specialist: {}", specialist.getEmail());
            jwTokenRepository.save(createJWTokenObject(specialist, accessToken));
        }
    }

    /**
     * Create a JWT token object.
     * @param obj The user/specialist for whom the token is created.
     * @param accessToken The JWT access token to be saved.
     * @return The created JWT token object.
     */
    private JwtToken createJWTokenObject(Object obj, String accessToken) {
        if(obj instanceof User user){
            return JwtToken.builder()
                    .user(user)
                    .token(accessToken)
                    .type(TokenType.BEARER)
                    .isExpired(false)
                    .isRevoked(false)
                    .build();
        } else {
            return JwtToken.builder()
                    .specialist((Specialist) obj)
                    .token(accessToken)
                    .type(TokenType.BEARER)
                    .isExpired(false)
                    .isRevoked(false)
                    .build();
        }
    }

    /**
     * Return a user if present in the database.
     * @param email The email address of the user to be returned.
     * @return The user if present in the database.
     * @throws InvalidLoginException if the user is not found in the database.
     */
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

    /**
     * Create a login response object.
     * @param userOrSpecialist The user/specialist for whom the login response is created.
     * @param message The message to be displayed in the response.
     * @param accessToken The JWT access token for the user/specialist.
     * @param refreshToken The JWT refresh token for the user/specialist.
     * @param secretImageUri The secret QR image URI for the user/specialist.
     * @param mfaEnabled A boolean value indicating if MFA is enabled for the user/specialist.
     * @return The created login response object.
     */
    public LoginResponse createLoginResponse(Object userOrSpecialist,String message, String accessToken, String refreshToken,String secretImageUri, boolean mfaEnabled) {
        if (userOrSpecialist instanceof User user) {
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
        } else if (userOrSpecialist instanceof Specialist specialist) {
            return LoginResponse.builder()
                    .path(request.getRequestURI())
                    .message(message)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .emailAddress(specialist.getEmail())
                    .secretImageUri(secretImageUri)
                    .mfaEnabled(mfaEnabled)
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.OK)
                    .build();
        }

        return null;
    }

    /**
     * Create a registration response object.
     * @param message The message to be displayed in the response.
     * @param emailAddress The email address of the user/specialist.
     * @param status The HTTP status of the response.
     * @return The created registration response object.
     */
    public RegistrationResponse createRegistrationResponse(String message, String emailAddress, HttpStatus status) {
        return RegistrationResponse.builder()
                .path(request.getRequestURI())
                .message(message)
                .emailAddress(emailAddress)
                .timestamp(LocalDateTime.now())
                .status(status)
                .build();
    }

    /**
     * Return a city if present in the database.
     * @param cityId The ID of the city to be returned.
     * @return The city if present in the database.
     * @throws CityServiceException if the city is not found in the database.
     */
    public City validateCityAndProceed(Long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> {
                    dcLogger.warn("City not found: {}", cityId);
                    throw new CityServiceException(CityResponse.builder()
                            .path(request.getRequestURI())
                            .message("City not found.")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });
    }

    /**
     * Return a country if present in the database.
     * @param countryId The ID of the country to be returned.
     * @return The country if present in the database.
     * @throws CountryServiceException if the country is not found in the database.
     */
    public Country validateCountryAndProceed(Long countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> {
                    dcLogger.warn("Country not found: {}", countryId);
                    throw new CountryServiceException(CountryResponse.builder()
                            .path(request.getRequestURI())
                            .message("Country not found.")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });
    }

    /**
     * Return a specialty if present in the database.
     * @param specialtyId The ID of the specialty to be returned.
     * @return The specialty if present in the database.
     * @throws SpecialtyServiceException if the specialty is not found in the database.
     */
    public Specialty validateSpecialtyAndProceed(Long specialtyId) {
        return specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> {
                    dcLogger.warn("Specialty not found: {}", specialtyId);
                    throw new SpecialtyServiceException(SpecialtyResponse.builder()
                            .path(request.getRequestURI())
                            .message("Specialty not found.")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });
    }

    /**
     * Extract addresses from the request, save them and return a list.
     * @param addresses The addresses to be extracted.
     * @return The list of extracted addresses.
     */
    public List<SpecialistAddress> extractAddresses(List<String> addresses) {
        List<SpecialistAddress> specialistAddresses = new ArrayList<>();

        for (String address : addresses) {
            dcLogger.info("Extracting address: {}", address);
            SpecialistAddress specialistAddress = SpecialistAddress.builder()
                    .docAddress(address)
                    .build();
            specialistAddressRepository.save(specialistAddress);
            specialistAddresses.add(specialistAddress);
        }

        return specialistAddresses;
    }

    /**
     * Return a specialist if present in the database.
     * @param emailAddress The email address of the specialist to be returned.
     * @return The specialist if present in the database.
     * @throws InvalidLoginException if the specialty is not found in the database.
     */
    public Specialist returnSpecialistIfPresent(String emailAddress) {
        return specialistRepository.findByEmail(emailAddress)
                .orElseThrow(() -> {
                    dcLogger.warn("Specialist not found: {}", emailAddress);
                    throw new InvalidLoginException(AuthenticationError.builder()
                            .path(request.getRequestURI())
                            .error("Specialist not found.")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });
    }

    /**
     * Create a verification response object.
     * @param message The message to be displayed in the response.
     * @param emailAddress The email address of the user/specialist.
     * @param status The HTTP status of the response.
     * @return The created verification response object.
     */
    public VerificationResponse createVerificationResponse(String message, String emailAddress, HttpStatus status) {
        return VerificationResponse.builder()
                .path(request.getRequestURI())
                .message(message)
                .emailAddress(emailAddress)
                .timestamp(LocalDateTime.now())
                .status(status)
                .build();
    }

    /**
     * Create a forgot password response object.
     * @param message The message to be displayed in the response.
     * @param emailAddress The email address of the user/specialist.
     * @param status The HTTP status of the response.
     * @return The created forgot password response object.
     */
    public ForgotPasswordResponse createForgotPasswordResponse(String message, String emailAddress, HttpStatus status) {
        return ForgotPasswordResponse.builder()
                .path(request.getRequestURI())
                .message(message)
                .emailAddress(emailAddress)
                .timestamp(LocalDateTime.now())
                .status(status)
                .build();
    }

    /**
     * Validate a password token and proceed with the password reset process if the token is valid.
     * @param token The password token to be validated.
     * @return The validated password token.
     * @throws InvalidEmailTokenException if the token is not found or is already used.
     */
    public PasswordToken validatePasswordTokenAndProceed(String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token))
                .flatMap(byToken -> byToken
                        .filter(
                                reset ->
                                        reset.getUsedAt() == null
                                                && reset
                                                .getExpiresAt()
                                                .isAfter(LocalDateTime.now())))
                .orElseThrow(
                        () -> {
                            dcLogger.warn("Token not found or is already used!");
                            throw new InvalidEmailTokenException(EmailError.builder()
                                    .path(request.getRequestURI())
                                    .error("Token not found or is already used!")
                                    .timestamp(LocalDateTime.now())
                                    .status(HttpStatus.BAD_REQUEST)
                                    .build());
                        });
    }

    /**
     * Extracts the full name from a provided name string.
     *
     * @param name The input name string from which the full name is to be extracted.
     * @return An array containing the first and last names extracted from the input name.
     * @throws FullNameNotFoundException If the full name or last name is not found in the input name.
     */
    public String[] extractFullName(String name) {
        String[] nameParts = name.split(" ", 2);

        if (nameParts.length >= 2) {
            dcLogger.info("Full name validated and extracted successfully: {}", Arrays.toString(nameParts));
            return nameParts;
        } else if (nameParts.length == 1) {
            dcLogger.warn("Only first name was found through the provider.");
            throw new FullNameNotFoundException(AuthenticationError.builder()
                    .path(request.getRequestURI())
                    .error("Only first name was found through your provider!")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.NOT_FOUND)
                    .build());
        } else {
            dcLogger.warn("Full name is not available from the provider.");
            throw new FullNameNotFoundException(AuthenticationError.builder()
                    .path(request.getRequestURI())
                    .error("Full name is not available from the provider.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.NOT_FOUND)
                    .build());
        }
    }
}
