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

@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final HttpCookieRequestRepository httpCookieRequestRepository;
    private final DCLogger dcLogger = new DCLogger(OAuth2FailureHandler.class);

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
