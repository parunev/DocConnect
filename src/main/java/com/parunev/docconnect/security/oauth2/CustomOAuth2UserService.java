package com.parunev.docconnect.security.oauth2;

import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.UserProfile;
import com.parunev.docconnect.models.enums.AuthProvider;
import com.parunev.docconnect.models.enums.Role;
import com.parunev.docconnect.repositories.UserProfileRepository;
import com.parunev.docconnect.repositories.UserRepository;
import com.parunev.docconnect.security.exceptions.InvalidLoginException;
import com.parunev.docconnect.security.oauth2.user.OAuth2UserInfo;
import com.parunev.docconnect.security.oauth2.user.OAuth2UserInfoFactory;
import com.parunev.docconnect.security.payload.AuthenticationError;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.validators.AuthHelpers;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Custom implementation of the OAuth2 user service that handles the processing of OAuth2 user information.
 * This service extends the Spring Security's DefaultOAuth2UserService and is responsible for loading and processing
 * OAuth2 user details obtained from an OAuth2 provider (e.g., Google, Facebook).
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final HttpServletRequest request;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final AuthHelpers authHelpers;
    private final DCLogger dcLogger = new DCLogger(CustomOAuth2UserService.class);

    /**
     * Load and process the OAuth2 user information.
     *
     * @param oAuth2UserRequest The OAuth2 user request containing information about the OAuth2 provider.
     * @return The processed OAuth2 user with additional details.
     * @throws OAuth2AuthenticationException If there's an authentication error during OAuth2 user processing.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        String correlationId = UUID.randomUUID().toString();

        DCLogger.setDCLoggerProperties(correlationId, request);
        dcLogger.info("Loading OAuth2 user: {} {}"
                , oAuth2UserRequest.getClientRegistration().getClientName()
                , oAuth2UserRequest.getClientRegistration().getProviderDetails().getIssuerUri());

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            dcLogger.error("Error processing OAuth2 user: {}", ex, ex.getMessage());
            throw new InvalidLoginException(AuthenticationError.builder()
                    .path(request.getRequestURI())
                    .error("Error processing OAuth2 user: %s %s".formatted(ex.getMessage(), ex.getCause()))
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.UNAUTHORIZED)
                    .build());
        } catch (Exception ex) {
            dcLogger.error("Internal authentication service error: {}", ex, ex.getMessage());
            throw new InvalidLoginException(AuthenticationError.builder()
                    .path(request.getRequestURI())
                    .error("Internal authentication service error: %s %s %s".formatted(ex, ex.getMessage(), ex.getCause()))
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build());
        }
    }

    /**
     * Process the OAuth2 user information, either by updating an existing user or registering a new OAuth2 user.
     *
     * @param oAuth2UserRequest The OAuth2 user request containing information about the OAuth2 provider.
     * @param oAuth2User        The OAuth2 user information obtained from the provider.
     * @return The processed OAuth2 user with additional details.
     */
    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        dcLogger.info("Processing OAuth2 user: {}", oAuth2User.getAttributes().get("email"));

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail())
                .orElse(null);

        if (user != null) {
            dcLogger.info("Updating existing user: {}", oAuth2UserInfo.getEmail());
            return updateExistingUser(user, oAuth2UserInfo);
        } else {
            dcLogger.info("Registering new OAuth2 user: {}", oAuth2UserInfo.getEmail());
            return registerNewOAuth2User(oAuth2UserInfo);
        }
    }

    /**
     * Register a new OAuth2 user with the provided user information.
     *
     * @param oAuth2UserInfo The OAuth2 user information.
     * @return The newly registered OAuth2 user.
     */
    private OAuth2User registerNewOAuth2User(OAuth2UserInfo oAuth2UserInfo) {
        dcLogger.info("Registering new OAuth2 user. Name: {} Email: {}",
                oAuth2UserInfo.getName(), oAuth2UserInfo.getEmail());

        User user = userRepository.save(User.builder()
                .provider(AuthProvider.GOOGLE)
                .providerId(oAuth2UserInfo.getId())
                .firstName(authHelpers.extractFullName(oAuth2UserInfo.getName())[0])
                .lastName(authHelpers.extractFullName(oAuth2UserInfo.getName())[1])
                .email(oAuth2UserInfo.getEmail())
                .imageUrl(oAuth2UserInfo.getImageUrl())
                .role(Role.ROLE_USER)
                .isEnabled(true)
                .build());
        dcLogger.info("New OAuth2 user registered successfully: {}", user.getEmail());

        userProfileRepository.save(UserProfile.builder()
                .feedbackNotification(true)
                .canceledNotification(true)
                .upcomingNotification(true)
                .user(user)
                .build());
        dcLogger.info("New profile was created for OAuth2 user: {}", user.getEmail());

        return user;
    }

    /**
     * Update the information of an existing user with the provided OAuth2 user information.
     *
     * @param user          The existing user to be updated.
     * @param oAuth2UserInfo The OAuth2 user information.
     * @return The updated OAuth2 user.
     */
    private OAuth2User updateExistingUser(User user, OAuth2UserInfo oAuth2UserInfo) {
        dcLogger.info("Updating existing user with OAuth2 information: {}", user.getEmail());

        user.setFirstName(authHelpers.extractFullName(oAuth2UserInfo.getName())[0]);
        user.setLastName(authHelpers.extractFullName(oAuth2UserInfo.getName())[1]);
        user.setImageUrl(oAuth2UserInfo.getImageUrl());

        return userRepository.save(user);
    }
}
