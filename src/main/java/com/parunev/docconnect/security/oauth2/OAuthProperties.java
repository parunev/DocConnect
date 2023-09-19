package com.parunev.docconnect.security.oauth2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for OAuth2 authentication.
 * This class is used to specify configuration properties related to OAuth2 authentication, particularly
 * authorized redirect URIs for the OAuth2 clients.
 */
@Data
@ConfigurationProperties(prefix = "oauth")
public class OAuthProperties {

    /**
     * An array of authorized redirect URIs for OAuth2 clients.
     * Redirect URIs are URLs to which the OAuth2 authorization server redirects the user after the user
     * grants or denies permission. These URIs are configured by OAuth2 clients during registration with
     * the authorization server.
     *
     * @see <a href="https://tools.ietf.org/html/rfc6749#section-3.1.2">OAuth2 RFC Section 3.1.2</a>
     */
    private String[] authorizedRedirectUris;
}
