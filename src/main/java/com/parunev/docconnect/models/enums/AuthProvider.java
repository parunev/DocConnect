package com.parunev.docconnect.models.enums;

import lombok.Getter;

/**
 * The `AuthProvider` enum defines the various authentication providers supported
 * by the application. Each authentication provider has a unique name associated
 * with it.
 * <p>
 * Enum Values:
 * - `LOCAL`: Represents the local authentication provider, where users
 *   authenticate using username and password within the application.
 * - `GOOGLE`: Represents the Google authentication provider, where users
 *   authenticate using their Google accounts.
 * <p>
 * Usage:
 * You can use the `AuthProvider` enum to specify the authentication provider
 * for a user or to determine the authentication method for a particular user.
 * For example:
 * <p>
 * ```java
 * // Set the authentication provider for a user * .setAuthProvider(AuthProvider.LOCAL);
 * <p>
 * // Check the authentication provider for a user
 * if (user.getAuthProvider() == AuthProvider.GOOGLE) {
 *     // User is authenticated via Google.
 *     // Perform specific actions for Google-authenticated users.
 * }
 * ```
 *
 * @see lombok.Getter
 */
@Getter
public enum AuthProvider {

    /**
     * Represents the local authentication provider.
     */
    LOCAL("local"),

    /**
     * Represents the Google authentication provider.
     */
    GOOGLE("google");

    /**
     * The unique name associated with the authentication provider.
     */
    private final String provider;

    /**
     * Constructs an `AuthProvider` enum with the specified provider name.
     *
     * @param provider The name of the authentication provider.
     */
    AuthProvider(String provider) {
        this.provider = provider;
    }
}
