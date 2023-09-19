package com.parunev.docconnect.security.oauth2;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.parunev.docconnect.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * HTTP Cookie-based repository for storing and retrieving OAuth2 authorization requests.
 * This class is responsible for managing OAuth2 authorization requests using HTTP cookies.
 * It implements the Spring Security `AuthorizationRequestRepository` interface to load, save, and remove
 * OAuth2 authorization requests from cookies.
 */

@Primary
@Component
public class HttpCookieRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    /**
     * The name of the HTTP cookie used to store OAuth2 authorization requests.
     */
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";

    /**
     * The name of the HTTP cookie used to store the redirect URI parameter.
     */
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

    /**
     * The expiration time (in seconds) for the stored cookies.
     */
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    /**
     * Load an OAuth2 authorization request from an HTTP cookie.
     *
     * @param request The HTTP request from which to load the authorization request.
     * @return The loaded OAuth2 authorization request, or null if not found.
     */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> {
                    try {
                        return CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .orElse(null);
    }

    /**
     * Save an OAuth2 authorization request to HTTP cookies.
     *
     * @param authorizationRequest The OAuth2 authorization request to save.
     * @param request              The HTTP request associated with the authorization request.
     * @param response             The HTTP response to which the cookies will be added.
     */
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest
            , HttpServletRequest request
            , HttpServletResponse response) {

        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            return;
        }

        try {
            CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                    CookieUtils.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin,
                    COOKIE_EXPIRE_SECONDS);
        }
    }

    /**
     * Remove and return an OAuth2 authorization request from cookies.
     *
     * @param request  The HTTP request associated with the authorization request.
     * @param response The HTTP response from which to remove the cookies.
     * @return The removed OAuth2 authorization request, or null if not found.
     */
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    /**
     * Remove OAuth2 authorization request-related cookies from the HTTP response.
     *
     * @param request  The HTTP request associated with the cookies.
     * @param response The HTTP response from which to remove the cookies.
     */
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}
