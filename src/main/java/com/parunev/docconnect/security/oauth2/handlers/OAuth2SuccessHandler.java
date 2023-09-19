package com.parunev.docconnect.security.oauth2.handlers;

import com.parunev.docconnect.models.JwtToken;
import com.parunev.docconnect.models.User;
import com.parunev.docconnect.models.enums.TokenType;
import com.parunev.docconnect.repositories.JwtTokenRepository;
import com.parunev.docconnect.security.exceptions.InvalidLoginException;
import com.parunev.docconnect.security.jwt.JwtService;
import com.parunev.docconnect.security.oauth2.HttpCookieRequestRepository;
import com.parunev.docconnect.security.oauth2.OAuthProperties;
import com.parunev.docconnect.security.payload.AuthenticationError;
import com.parunev.docconnect.utils.CookieUtils;
import com.parunev.docconnect.utils.DCLogger;
import com.parunev.docconnect.utils.validators.AuthHelpers;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static com.parunev.docconnect.security.oauth2.HttpCookieRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * Handler for handling successful OAuth2 authentication.
 * This class is responsible for handling successful OAuth2 authentication. It extends Spring Security's
 * SimpleUrlAuthenticationSuccessHandler and overrides the onAuthenticationSuccess method to perform custom handling.
 * After successful authentication, it generates JWT tokens, revokes previous tokens for the user, and redirects the
 * user to the appropriate target URL, including the access and refresh tokens as query parameters.
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final HttpCookieRequestRepository httpCookieRepository;
    private final OAuthProperties oAuthProperties;
    private final AuthHelpers authHelpers;
    private final JwtTokenRepository jwtTokenRepository;
    private final DCLogger dcLogger = new DCLogger(OAuth2SuccessHandler.class);

    /**
     * Handle successful OAuth2 authentication.
     *
     * @param request        The HTTP servlet request.
     * @param response       The HTTP servlet response.
     * @param authentication The authentication object representing the authenticated user.
     * @throws IOException If an I/O error occurs during the handling of the success.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            dcLogger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        dcLogger.info("OAuth2 authentication success for user: {}", userDetails.getUsername());

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * Determine the target URL for redirection after successful authentication.
     *
     * @param request        The HTTP servlet request.
     * @param response       The HTTP servlet response.
     * @param authentication The authentication object representing the authenticated user.
     * @return The target URL for redirection, including access and refresh tokens as query parameters.
     */
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            dcLogger.warn("Unauthorized Redirect URI: {}", redirectUri.get());
            throw new InvalidLoginException(AuthenticationError.builder()
                    .path(request.getRequestURI())
                    .error("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication")
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.UNAUTHORIZED)
                    .build());
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        authHelpers.revokeUserTokens(user);

        JwtToken jwtToken = JwtToken.builder()
                .user(user)
                .token(accessToken)
                .type(TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();

        jwtTokenRepository.save(jwtToken);

        dcLogger.info("Redirecting user after OAuth2 authentication success to: {}", targetUrl);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();
    }

    /**
     * Clear authentication-related attributes and cookies.
     *
     * @param request  The HTTP servlet request.
     * @param response The HTTP servlet response.
     */
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieRepository.removeAuthorizationRequestCookies(request, response);
    }

    /**
     * Check if the provided URI is an authorized redirect URI.
     *
     * @param uri The URI to check.
     * @return True if the URI is an authorized redirect URI, otherwise false.
     */
    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return Stream.of(oAuthProperties.getAuthorizedRedirectUris())
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}
