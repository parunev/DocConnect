package com.parunev.docconnect.models.payloads.user.profile;

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
@Schema(description = "Password change response", name = "Password Change Response")
public class PasswordChangeResponse {

    @Schema(description = "Path", example = "/api/v1/user-profile/change-password")
    private String path;

    @Schema(description = "User email", example = "example@gmail.com")
    private String email;

    @Schema(description = "User full name", example = "John Doe")
    private String fullName;

    @Schema(description = "Message", example = "Password changed successfully")
    private String message;

    @Schema(description = "Timestamp", example = "2021-08-01T12:00:00.000+00:00")
    private LocalDateTime timestamp;

    @Schema(description = "Status", example = "200")
    private HttpStatus status;
}
