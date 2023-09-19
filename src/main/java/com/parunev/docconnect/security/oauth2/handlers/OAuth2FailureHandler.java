package com.parunev.docconnect.security.oauth2.handlers;

import com.parunev.docconnect.security.oauth2.HttpCookieRequestRepository;
import com.parunev.docconnect.utils.CookieUtils;
import com.parunev.docconnect.utils.DCLogger;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.parunev.docconnect.security.oauth2.HttpCookieRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * Handler for handling OAuth2 authentication failures.
 * This class is responsible for handling authentication failures during the OAuth2 authentication process. It extends
 * the Spring Security's SimpleUrlAuthenticationFailureHandler and overrides the onAuthenticationFailure method to
 * perform custom handling. When an OAuth2 authentication failure occurs, this handler logs the error, redirects the
 * user to an appropriate error page, and removes any stored authorization request cookies.
 */
@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final HttpCookieRequestRepository httpCookieRequestRepository;
    private final DCLogger dcLogger = new DCLogger(OAuth2FailureHandler.class);

    /**
     * Handle OAuth2 authentication failure.
     *
     * @param request    The HTTP servlet request.
     * @param response   The HTTP servlet response.
     * @param exception  The authentication exception.
     * @throws IOException If an I/O error occurs during the handling of the failure.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        dcLogger.error("OAuth2 authentication failure: {}", exception, exception.getMessage());
        String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(("/"));

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();

        httpCookieRequestRepository.removeAuthorizationRequestCookies(request, response);

        dcLogger.info("Redirecting user after OAuth2 authentication failure to: {}", targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
