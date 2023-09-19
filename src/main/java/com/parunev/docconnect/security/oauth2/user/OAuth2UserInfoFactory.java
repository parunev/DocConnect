package com.parunev.docconnect.security.oauth2.user;

import com.parunev.docconnect.security.exceptions.InvalidLoginException;
import com.parunev.docconnect.security.payload.AuthenticationError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Factory class for creating OAuth2UserInfo instances based on the OAuth2 provider registration ID.
 * This factory class provides a method for creating instances of OAuth2UserInfo, which represent user information
 * obtained from various OAuth2 providers. It determines the provider based on the registration ID and returns
 * an appropriate OAuth2UserInfo implementation or throws an exception if the provider is not supported.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2UserInfoFactory {

    /**
     * Create an instance of OAuth2UserInfo based on the OAuth2 provider registration ID.
     *
     * @param registrationId The registration ID of the OAuth2 provider.
     * @param attributes     A map of user attributes obtained from the OAuth2 provider.
     * @return An instance of OAuth2UserInfo specific to the provider.
     * @throws InvalidLoginException If the provider is not supported, an exception is thrown.
     */
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("google")) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new InvalidLoginException(AuthenticationError.builder()
                    .error("Sorry! Login with " + registrationId + " is not supported yet.")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }
}
