package com.parunev.docconnect.models.payloads.user.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Login Response", description = "Payload for login response")
public class LoginResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Path", description = "The path of the request", example = "/api/v1/auth/login")
    private String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Response message", description = "The message from the server", example = "Logged in successfully")
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "JWT Access Token", description = "Short expiration time token used for authentication")
    private String accessToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "JWT Refresh Token", description = "Long expiration time token used for refreshing the access token")
    private String refreshToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Secret Image URI", description = "The URI of the secret QR image used for 2FA")
    private String secretImageUri;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "MFA Enabled", description = "The status of the MFA", example = "true")
    private boolean mfaEnabled;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Email Address", description = "The email address of the logged user", example = "example@gmail.com")
    private String emailAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Timestamp", description = "The timestamp of the response", example = "2021-09-26T20:00:00.000Z")
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Status", description = "The status of the response", example = "OK")
    private HttpStatus status;
}
