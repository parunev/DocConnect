package com.parunev.docconnect.security.oauth2.user;

import java.util.Map;

/**
 * Abstract sealed class for extracting user information from OAuth2 providers.
 * This abstract class provides a common structure for extracting user information from various OAuth2 providers.
 * It defines methods for retrieving user attributes such as ID, name, email, and profile picture URL. Subclasses
 * specific to each OAuth2 provider implement these methods to extract provider-specific attributes.
 */
public abstract sealed class OAuth2UserInfo permits GoogleOAuth2UserInfo{
    protected Map<String, Object> attributes;

    /**
     * A map containing user attributes obtained from the OAuth2 provider.
     */
    protected OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * Get the user attributes as a map.
     *
     * @return A map containing user attributes.
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * Get the user's unique ID from the user attributes.
     *
     * @return The user's unique ID.
     */
    public abstract String getId();

    /**
     * Get the user's full name from the user attributes.
     *
     * @return The user's full name.
     */
    public abstract String getName();

    /**
     * Get the user's email address from the user attributes.
     *
     * @return The user's email address.
     */
    public abstract String getEmail();

    /**
     * Get the URL of the user's profile picture from the user attributes.
     *
     * @return The URL of the user's profile picture.
     */
    public abstract String getImageUrl();
}
