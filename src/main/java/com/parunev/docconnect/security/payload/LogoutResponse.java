package com.parunev.docconnect.security.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(name = "Logout Response", description = "Response object for logout")
public class LogoutResponse {

    @Schema(name = "Path", description = "The path of the request", example = "/api/v1/logout")
    private String path;

    @Schema(name = "Message", description = "The message of the response", example = "Successfully logged out")
    private String message;

    @Schema(name = "Timestamp", description = "The timestamp of the response", example = "2021-09-26T20:00:00.000Z")
    private LocalDateTime timestamp;

    @Schema(name = "Status", description = "The status of the response", example = "OK")
    private HttpStatus status;
}
