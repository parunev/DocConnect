package com.parunev.docconnect.models.payloads.user.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Verification Response", description = "Payload for verification response")
public class VerificationResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Path", description = "The path of the request", example = "/api/v1/auth/login/send-code")
    private String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Response message", description = "The message from the server", example = "Logged in successfully")
    private String message;

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
