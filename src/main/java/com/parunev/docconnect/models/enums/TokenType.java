package com.parunev.docconnect.models.enums;

/**
 * The `TokenType` enum represents the type of tokens used for authentication
 * or authorization in the application. Currently, it defines a single token
 * type: "BEARER."
 * <p>
 * Enum Values:
 * - `BEARER`: Represents the "Bearer" token type often used in authentication
 *   headers in APIs, such as OAuth 2.0.
 * <p>
 * Usage:
 * The `TokenType` enum can be used to specify the type of tokens used for
 * authentication and authorization purposes in the application. For example:
 * <p>
 * ```java
 * // Set the token type for an authentication request
 * authenticationRequest.setTokenType(TokenType.BEARER);
 * <p>
 * // Check the token type in an authentication response
 * if (authenticationResponse.getTokenType() == TokenType.BEARER) {
 *     // Perform actions for "Bearer" tokens.
 * }
 * ```
 */
public enum TokenType {

    /**
     * Represents the "Bearer" token type.
     */
    BEARER
}
