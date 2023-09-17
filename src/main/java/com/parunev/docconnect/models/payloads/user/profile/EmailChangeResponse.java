package com.parunev.docconnect.models.payloads.user.profile;

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
@Schema(name = "Email Change Response", description = "Response for changing user email.")
public class EmailChangeResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Path", description = "The path of the request", example = "/api/v1/profile")
    private String path;

    @Schema(name = "User new email", description = "User new email.")
    private String newEmail;

    @Schema(name = "Message", description = "Message for changing user email.")
    private String message;

    @Schema(name = "User enabled", description = "User enabled status after changing email.")
    private boolean enabled;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Timestamp", description = "The timestamp of the response", example = "2021-09-26T20:00:00.000Z")
    private LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(name = "Status", description = "The status of the response", example = "OK")
    private HttpStatus status;
}
