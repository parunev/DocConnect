package com.parunev.docconnect.services;

import com.parunev.docconnect.models.ConfirmationToken;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.UserProfile;
import com.parunev.docconnect.models.payloads.user.profile.*;
import com.parunev.docconnect.repositories.UserProfileRepository;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.security.exceptions.EmailAlreadyExistsAuthenticationException;
import com.parunev.docconnect.security.mfa.Google2FAuthentication;
import com.parunev.docconnect.security.payload.AuthenticationError;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.email.EmailSender;
import com.parunev.docconnect.utils.validators.AuthHelpers;
import com.parunev.docconnect.utils.validators.UserProfileHelpers;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.parunev.docconnect.services.AuthService.CONFIRMATION_LINK;
import static com.parunev.docconnect.utils.email.Patterns.buildConfirmationEmail;
import static com.parunev.docconnect.utils.email.Patterns.buildPasswordChangedEmail;

/**
 * The `UserProfileService` class provides services for managing user profiles and related operations.
 * It includes methods for updating user profiles, changing passwords, and changing email addresses.
 * The class integrates with user repositories, confirmation tokens, and authentication utilities.
 */
@Service
@Validated
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserProfileHelpers userProfileHelpers;
    private final AuthHelpers authHelpers;
    private final Google2FAuthentication google2FAuthentication;
    private final EmailSender emailSender;
    private final DCLogger dcLogger = new DCLogger(UserProfileService.class);

    /**
     * Updates the user's profile information based on the provided request.
     *
     * @param request The profile update request containing the new user information.
     * @return A response containing the updated user profile information.
     */
    public ProfileResponse updateUserProfile(@Valid ProfileRequest request){
        dcLogger.info("Updating user profile.");
        User user = userProfileHelpers.findUser();
        UserProfile profile = userProfileRepository.findByUserId(user.getId());

        updateProfile(user, profile, request);

        dcLogger.info("Returning updated profile for user: {}", profile.getUser().getEmail());
        return ProfileResponse.builder()
                .path(userProfileHelpers.getRequest().getRequestURI())
                .emailAddress(user.getEmail())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .country(user.getCountry().getCountryName())
                .city(user.getCity().getCityName())
                .imageUrl(user.getImageUrl())
                .weight(profile.getWeight())
                .height(profile.getHeight())
                .bloodPressure(profile.getBloodPressure())
                .bloodSugarLevel(profile.getBloodSugarLevel())
                .upcomingNotification(profile.isUpcomingNotification())
                .canceledNotification(profile.isCanceledNotification())
                .feedbackNotification(profile.isFeedbackNotification())
                .mfaEnabled(profile.getUser().isMfaEnabled())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Changes the user's password based on the provided password change request.
     *
     * @param passwordChangeRequest The request containing old and new password information.
     * @return A response indicating the status of the password change operation.
     */
    public PasswordChangeResponse changeUserPassword(@Valid PasswordChangeRequest passwordChangeRequest){
        User user = userProfileHelpers.findUser();

        if (userProfileHelpers.checkProvider(user.getProvider())
          && (userProfileHelpers.checkPasswordMatching(passwordChangeRequest.getOldPassword(), user.getPassword())
          && (userProfileHelpers.checkNewPassword(passwordChangeRequest.getNewPassword(), passwordChangeRequest.getConfirmNewPassword())))){

            dcLogger.info("Updating password for user: {}", user.getEmail());
            user.setPassword(userProfileHelpers
                    .getPasswordEncoder()
                    .encode(passwordChangeRequest.getNewPassword()));
            userRepository.save(user);

            dcLogger.info("Sending email to user: {}", user.getEmail());
            emailSender.send(user.getEmail(), buildPasswordChangedEmail(user.getName()), "DocConnect: Password changed successfully.");
        }
        return PasswordChangeResponse.builder()
                .path(userProfileHelpers.getRequest().getRequestURI())
                .email(user.getEmail())
                .fullName(user.getName())
                .message("Password changed successfully.")
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Changes the user's email address and sends a confirmation email for the new address.
     *
     * @param request The email change request containing the new email address.
     * @return A response indicating the status of the email change operation.
     */
    public EmailChangeResponse changeUserEmail(@Valid EmailChangeRequest request){
        User user = userProfileHelpers.findUser();

        boolean doExist = userRepository.findByEmail(request.getEmail()).isPresent();

        if (user.getEmail().equals(request.getEmail())){
            dcLogger.warn("Email is the same as your current: {}", request.getEmail());
            throw new EmailAlreadyExistsAuthenticationException(
                    AuthenticationError.builder()
                            .path(userProfileHelpers.getRequest().getRequestURI())
                            .error("Email is the same as your current.")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        } else if (doExist){
            dcLogger.warn("Email already exists: {}", request.getEmail());
            throw new EmailAlreadyExistsAuthenticationException(
                    AuthenticationError.builder()
                            .path(userProfileHelpers.getRequest().getRequestURI())
                            .error("Email already exists.")
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.BAD_REQUEST)
                            .build()
            );
        }

        user.setEmail(request.getEmail());
        user.setEnabled(false);
        dcLogger.info("Setting user settings isEnabled: {}, newEmail: {}", user.isEnabled(), user.getEmail());
        userRepository.save(user);

        dcLogger.info("Delete authorization for user {}", user.getEmail());
        authHelpers.revokeUserTokens(user);

        dcLogger.info("Sending new email confirmation to user: {}", user.getEmail());
        ConfirmationToken token = ConfirmationToken.builder()
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        confirmationTokenService.saveConfirmationToken(token);
        emailSender.send(user.getEmail()
                , buildConfirmationEmail(user.getName(), CONFIRMATION_LINK + token.getToken())
                , "DocConnect: Confirm your new email address.");

        dcLogger.info("Returning email change response for user: {}", user.getEmail());

        return EmailChangeResponse.builder()
                .path(userProfileHelpers.getRequest().getRequestURI())
                .newEmail(user.getEmail())
                .message("Email changed successfully. Please confirm your new email address.")
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Validates and applies the requested updates to the user's profile based on the provided request.
     *
     * @param user     The user entity to validate and potentially update.
     * @param profile  The user profile entity to validate and potentially update.
     * @param request  The profile update request containing the new user information.
     */
    private void updateProfile(User user, UserProfile profile, ProfileRequest request) {
        if (request.getFirstName() != null){
            dcLogger.info("Updating first name for user: {}", profile.getUser().getEmail());
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null){
            dcLogger.info("Updating last name for user: {}", profile.getUser().getEmail());
            user.setLastName(request.getLastName());
        }

        if (!user.isMfaEnabled() && request.isMfaEnabled()){
            dcLogger.info("Updating MFA enabled for user: {}", profile.getUser().getEmail());
            user.setMfaSecret(google2FAuthentication.generateNewSecret());
            user.setMfaEnabled(request.isMfaEnabled());
        } else if (user.isMfaEnabled() && !request.isMfaEnabled()){
            dcLogger.info("Updating MFA disabled for user: {}", profile.getUser().getEmail());
            user.setMfaEnabled(request.isMfaEnabled());
        }

        if (request.getImageUrl() != null){
            dcLogger.info("Updating image url for user: {}", profile.getUser().getEmail());
            user.setImageUrl(request.getImageUrl());
        }
        if (request.getCountryId() != null){
            dcLogger.info("Updating country id for user: {}", profile.getUser().getEmail());
            user.setCountry(userProfileHelpers.validateAndReturnCountry(request.getCountryId()));
        }
        if (request.getCityId() != null){
            dcLogger.info("Updating city id for user: {}", profile.getUser().getEmail());
            user.setCity(userProfileHelpers.validateAndReturnCity(request.getCityId()));
        }

        userRepository.save(user);

        if (request.isCanceledNotification() != profile.isCanceledNotification()){
            dcLogger.info("Updating canceled notification for user: {}", profile.getUser().getEmail());
            profile.setCanceledNotification(request.isCanceledNotification());
        }
        if (request.isFeedbackNotification() != profile.isFeedbackNotification()){
            dcLogger.info("Updating feedback notification for user: {}", profile.getUser().getEmail());
            profile.setFeedbackNotification(request.isFeedbackNotification());
        }
        if (request.isUpcomingNotification() != profile.isUpcomingNotification()){
            dcLogger.info("Updating upcoming notification for user: {}", profile.getUser().getEmail());
            profile.setUpcomingNotification(request.isUpcomingNotification());
        }

        profile.setWeight(request.getWeight());
        profile.setHeight(request.getHeight());
        profile.setBloodPressure(request.getBloodPressure());
        profile.setBloodSugarLevel(request.getBloodSugarLevel());

        userProfileRepository.save(profile);

        dcLogger.info("Profile updated successfully for user: {}", profile.getUser().getEmail());
    }
}
