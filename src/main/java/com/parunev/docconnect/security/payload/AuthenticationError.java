package com.parunev.docconnect.security.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(name = "Authentication Error", description = "Response object for authentication error")
public class AuthenticationError{

    @Schema(name = "Path", description = "The path of the request", example = "/api/v1/countries")
    private String path;

    @Schema(name = "Error", description = "The error of the request", example = "Failed to send email")
    private String error;

    @Schema(name = "Status", description = "The status of the response", example = "BAD_REQUEST")
    private HttpStatus status;

    @Schema(name = "Timestamp", description = "The timestamp of the response", example = "2021-09-26T20:00:00.000Z")
    private LocalDateTime timestamp;
}
