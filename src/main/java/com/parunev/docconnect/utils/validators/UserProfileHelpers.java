package com.parunev.docconnect.utils.validators;

import com.parunev.docconnect.models.City;
import com.parunev.docconnect.models.Country;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.enums.AuthProvider;
import com.parunev.docconnect.models.payloads.city.CityResponse;
import com.parunev.docconnect.models.payloads.country.CountryResponse;
import com.parunev.docconnect.repositories.CityRepository;
import com.parunev.docconnect.repositories.CountryRepository;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.security.exceptions.CityServiceException;
import com.parunev.docconnect.security.exceptions.CountryServiceException;
import com.parunev.docconnect.security.exceptions.InvalidPasswordChangeException;
import com.parunev.docconnect.security.exceptions.UserNotFoundException;
import com.parunev.docconnect.security.payload.AuthenticationError;
import com.parunev.docconnect.utils.DCLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * The `UserProfileHelpers` class provides utility methods for working with user profiles
 * and performing common validation tasks related to user operations. It encapsulates
 * functionality for retrieving the currently authenticated user, validating and returning
 * country and city information, checking user provider type, and password validation.
 */
@Data
@Component
@RequiredArgsConstructor
public class UserProfileHelpers {

    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;
    private final DCLogger dcLogger = new DCLogger(UserProfileHelpers.class);
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    /**
     * Retrieves the currently authenticated user from the SecurityContextHolder.
     *
     * @return The authenticated User object.
     * @throws UserNotFoundException If the user is not found in the database.
     */
    public User findUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = userDetails.getUsername();

        dcLogger.info("Validating and returning user by email: {}", userEmail);
        User user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> {
                    dcLogger.warn("User not found.");
                    throw new UserNotFoundException(AuthenticationError.builder()
                            .path(request.getRequestURI())
                            .error("User not present in the database.")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .build());
                });
        dcLogger.info("User found successfully: {}", user.getEmail());

        return user;
    }

    /**
     * Validates and returns country information by its ID.
     *
     * @param countryId The ID of the country to retrieve.
     * @return The validated Country object.
     * @throws CountryServiceException If the country with the provided ID is not found.
     */
    public Country validateAndReturnCountry(Long countryId) {
        return countryRepository.findById(countryId)
                .orElseThrow(() -> {
                    dcLogger.warn("Country with the provided Id not found: {}", countryId);
                    throw new CountryServiceException(
                            CountryResponse.builder()
                                    .path(request.getRequestURI())
                                    .message("Country with the provided Id not found.")
                                    .timestamp(LocalDateTime.now())
                                    .status(HttpStatus.NOT_FOUND)
                                    .build()
                    );
                });
    }

    /**
     * Validates and returns city information by its ID.
     *
     * @param cityId The ID of the city to retrieve.
     * @return The validated City object.
     * @throws CityServiceException If the city with the provided ID is not found.
     */
    public City validateAndReturnCity(Long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> {
                    dcLogger.warn("City with the provided Id not found: {}", cityId);
                    throw new CityServiceException(
                            CityResponse.builder()
                                    .path(request.getRequestURI())
                                    .message("City with the provided Id not found.")
                                    .timestamp(LocalDateTime.now())
                                    .status(HttpStatus.NOT_FOUND)
                                    .build()
                    );
                });
    }

    /**
     * Checks the authentication provider type for a user.
     *
     * @param provider The authentication provider to check.
     * @return `true` if the user is registered with a local provider, otherwise an exception is thrown.
     * @throws InvalidPasswordChangeException If the user is registered with a non-local provider.
     */
    public boolean checkProvider(AuthProvider provider) {
        if (provider.equals(AuthProvider.LOCAL)) {
            dcLogger.info("User is registered with local provider /PROCEED.");
        } else {
            dcLogger.warn("User is registered with google provider /ABORT.");
            throw new InvalidPasswordChangeException(AuthenticationError.builder()
                    .path(request.getRequestURI())
                    .error("Users registered with google provider cannot change their password.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        return true;
    }

    /**
     * Validates if the provided old password matches the one in the database.
     *
     * @param oldPassword The old password to validate.
     * @param password    The password stored in the database.
     * @return `true` if the old password matches, otherwise an exception is thrown.
     * @throws InvalidPasswordChangeException If the old password does not match the one in the database.
     */
    public boolean checkPasswordMatching(String oldPassword, String password) {
        if (passwordEncoder.matches(oldPassword, password)) {
            dcLogger.info("Old password matches the one in the database /PROCEED.");
            return true;
        } else {
            dcLogger.warn("Old password does not match the one in the database /ABORT.");
            throw new InvalidPasswordChangeException(AuthenticationError.builder()
                    .path(request.getRequestURI())
                    .error("Old password does not match the one in the database.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    /**
     * Validates if the new password matches the confirmation password.
     *
     * @param newPassword        The new password to validate.
     * @param confirmNewPassword The confirmation password to compare.
     * @return `true` if the new password matches the confirmation password, otherwise an exception is thrown.
     * @throws InvalidPasswordChangeException If the new password does not match the confirmation password.
     */
    public boolean checkNewPassword(String newPassword, String confirmNewPassword) {
        if (newPassword.equals(confirmNewPassword)) {
            dcLogger.info("New password matches the confirmation /PROCEED.");
        } else {
            dcLogger.warn("New password does not match the confirmation /ABORT.");
            throw new InvalidPasswordChangeException(AuthenticationError.builder()
                    .path(request.getRequestURI())
                    .error("New password does not match the confirmation password.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
        return true;
    }
}
