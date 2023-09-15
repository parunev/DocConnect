package com.parunev.docconnect.security.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@Schema(name = "Constraint Error", description = "Response object for constraint error")
public class ConstraintError {

        @Schema(name = "Path", description = "The path of the request", example = "/api/v1/countries")
        private String path;

        @Schema(name = "Errors", description = "The errors of the request", example = "Country name must be unique")
        private Map<String, String> errors;

        @Schema(name = "Status", description = "The status of the response", example = "BAD_REQUEST")
        private HttpStatus status;

        @Schema(name = "Timestamp", description = "The timestamp of the response", example = "2021-09-26T20:00:00.000Z")
        private LocalDateTime timestamp;
}
