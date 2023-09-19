package com.parunev.docconnect.security.oauth2.user;

import java.util.Map;

/**
 * Implementation of OAuth2UserInfo specific to Google OAuth2 provider.
 * This class extends the abstract OAuth2UserInfo class and provides specific implementations for extracting user
 * information from Google OAuth2 user attributes. It is used to parse and retrieve user details obtained from
 * the Google OAuth2 provider.
 */
public final class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    /**
     * Constructs a new GoogleOAuth2UserInfo instance with the provided user attributes.
     *
     * @param attributes A map of user attributes obtained from the Google OAuth2 provider.
     */
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     * Get the user's ID from the user attributes.
     *
     * @return The user's unique ID.
     */
    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    /**
     * Get the user's name from the user attributes.
     *
     * @return The user's full name.
     */
    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    /**
     * Get the user's email address from the user attributes.
     *
     * @return The user's email address.
     */
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    /**
     * Get the URL of the user's profile picture from the user attributes.
     *
     * @return The URL of the user's profile picture.
     */
    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}
