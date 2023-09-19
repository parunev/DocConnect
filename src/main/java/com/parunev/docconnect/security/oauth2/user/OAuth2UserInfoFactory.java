package com.parunev.docconnect.security.oauth2.user;

import com.parunev.docconnect.security.exceptions.InvalidLoginException;
import com.parunev.docconnect.security.payload.AuthenticationError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2UserInfoFactory {

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
