package com.parunev.docconnect.models.payloads.user.registration;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Registration Response", description = "Payload for registration response")
public class RegistrationResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Path", description = "The path of the request", example = "/api/v1/auth/register")
    private String path;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Response message", description = "The message from the server", example = "Country created successfully")
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Email Address", description = "The email of the registered user", example = "example@gmail.com")
    private String emailAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Timestamp", description = "The timestamp of the response", example = "2021-09-26T20:00:00.000Z")
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Status", description = "The status of the response", example = "OK")
    private HttpStatus status;
}
