package com.parunev.docconnect.security.oauth2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "oauth")
public class OAuthProperties {
    private String[] authorizedRedirectUris;
}
